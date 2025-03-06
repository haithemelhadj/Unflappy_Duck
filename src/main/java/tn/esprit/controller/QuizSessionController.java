package tn.esprit.controller;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Duration;
import tn.esprit.api.QuizApiClient;
import tn.esprit.models.Question;
import tn.esprit.services.ServiceQuestion;
import java.util.Collections;
import java.util.List;

public class QuizSessionController {
    @FXML private Label questionNumberLabel;
    @FXML private Label questionTextLabel;
    @FXML private VBox answersContainer;
    @FXML private Label progressLabel;
    @FXML private ProgressBar xpProgressBar;
    @FXML private Label xpLabel;

    private List<Question> questions;
    private int currentQuestionIndex = 0;
    private int score = 0;
    private int totalXP = 0;
    private final int XP_PER_QUESTION = 25;
    private Timeline timer;
    private int timeSeconds = 0;
    private Label timerLabel;

    private final ServiceQuestion questionService = new ServiceQuestion();

    @FXML
    public void initialize() {
        setupTimer();
        loadQuestions();
        if (!questions.isEmpty()) {
            showQuestion();
        }
        updateProgress();
    }

    private void setupTimer() {
        timerLabel = new Label("Time: 00:00");
        timerLabel.getStyleClass().add("timer-label");

        HBox timerBox = new HBox(timerLabel);
        timerBox.setAlignment(Pos.TOP_RIGHT);
        ((VBox) answersContainer.getParent()).getChildren().add(0, timerBox);

        timer = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            timeSeconds++;
            timerLabel.setText(String.format("Time: %02d:%02d",
                    timeSeconds / 60,
                    timeSeconds % 60));
        }));
        timer.setCycleCount(Timeline.INDEFINITE);
        timer.play();
    }

    private void loadQuestions() {
        questions = questionService.getQuestions();
        Collections.shuffle(questions);
    }

    private void showQuestion() {
        if (questions.isEmpty()) {
            questionNumberLabel.setText("No questions available.");
            questionTextLabel.setText("");
            answersContainer.getChildren().clear();
            return;
        }
        answersContainer.getChildren().clear();
        Question current = questions.get(currentQuestionIndex);

        questionNumberLabel.setText("Question #" + (currentQuestionIndex + 1));
        questionTextLabel.setText(current.getQuestion());

        ToggleGroup answerGroup = new ToggleGroup();
        addStyledAnswerButton(current.getReponse1(), "A", answerGroup);
        addStyledAnswerButton(current.getReponse2(), "B", answerGroup);
        addStyledAnswerButton(current.getReponse3(), "C", answerGroup);
        addStyledAnswerButton(current.getReponse4(), "D", answerGroup);
    }

    private void addStyledAnswerButton(String answer, String letter, ToggleGroup group) {
        RadioButton button = new RadioButton();
        button.getStyleClass().add("radio-button");
        button.setToggleGroup(group);
        button.setUserData(letter);

        HBox container = new HBox(10);
        Label letterLabel = new Label(letter + ".");
        letterLabel.getStyleClass().add("answer-letter");

        Text answerText = new Text(answer);
        answerText.setWrappingWidth(500);

        container.getChildren().addAll(letterLabel, answerText);
        button.setGraphic(container);
        answersContainer.getChildren().add(button);
    }

    @FXML
    private void handleNextQuestion() {
        checkAnswer();
        if(currentQuestionIndex < questions.size() - 1) {
            currentQuestionIndex++;
            showQuestion();
            updateProgress();
        } else {
            showResults();
        }
    }

    @FXML
    private void handlePreviousQuestion() {
        if(currentQuestionIndex > 0) {
            currentQuestionIndex--;
            showQuestion();
            updateProgress();
        }
    }

    private void checkAnswer() {
        RadioButton selected = (RadioButton) answersContainer.lookup(".radio-button:selected");
        if(selected != null) {
            String userAnswer = selected.getUserData().toString();
            String correctAnswer = questions.get(currentQuestionIndex).getReponseCorrecte();

            if(userAnswer.equalsIgnoreCase(correctAnswer)) {
                score++;
                totalXP += XP_PER_QUESTION;
                updateXPDisplay();
            }
        }
    }

    private void updateProgress() {
        progressLabel.setText("Question " + (currentQuestionIndex + 1) + " of " + questions.size());
    }

    private void updateXPDisplay() {
        int level = (totalXP / 100) + 1;
        int currentLevelXP = totalXP % 100;
        xpProgressBar.setProgress(currentLevelXP / 100.0);
        xpLabel.setText(currentLevelXP + "/100 XP (Level " + level + ")");
    }

    private void showResults() {
        timer.stop();
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Quiz Results");
        alert.setHeaderText("Quiz Completed!");
        alert.setContentText("Final Score: " + score + "/" + questions.size() + "\n"
                + "Total XP Earned: " + totalXP + "\n"
                + "Time Taken: " + timerLabel.getText());
        alert.showAndWait();
    }

    // NEW: Method to import quiz questions from an external API
    @FXML
    private void handleImportFromApi() {
        QuizApiClient apiClient = new QuizApiClient();
        try {
            List<Question> importedQuestions = apiClient.fetchQuestions();
            // Optionally assign a question number to each imported question.
            for (Question q : importedQuestions) {
                // For example, set question number as current total count + 1.
                q.setQno(questionService.getQuestions().size() + 1);
                questionService.addQuestion(q);
            }
            // Reload questions from the database.
            loadQuestions();
            showAlert("Info", "Imported " + importedQuestions.size() + " questions from API.");
        } catch (Exception e) {
            showAlert("Error", "Failed to import questions: " + e.getMessage());
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
