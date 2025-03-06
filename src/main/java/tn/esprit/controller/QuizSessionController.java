package tn.esprit.controller;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;
import tn.esprit.models.Question;
import tn.esprit.services.ServiceQuestion;
import javafx.scene.text.Text;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;

import java.util.*;

public class QuizSessionController {
    @FXML private Label questionNumberLabel;
    @FXML private Label questionTextLabel;
    @FXML private VBox answersContainer;
    @FXML private Label progressLabel;
    @FXML private ProgressBar xpProgressBar;
    @FXML private Label xpLabel;
    @FXML private Circle progressCircle;
    @FXML private Label difficultyLabel;

    private List<Question> questions;
    private int currentQuestionIndex = 0;
    private int score = 0;
    private int totalXP = 0;
    private final int XP_PER_QUESTION = 25;
    private Timeline timer;
    private int remainingTimeSeconds = 300;
    private Label timerLabel;
    private Map<Integer, String> userAnswers = new HashMap<>();
    private final ServiceQuestion questionService = new ServiceQuestion();

    @FXML
    public void initialize() {
        setupCountdownTimer();
        loadQuestions();
        if (!questions.isEmpty()) {
            showQuestion();
        }
        setupProgressCircle();
        updateProgress();
        updateXPDisplay();
    }

    private void setupProgressCircle() {
        progressCircle.setFill(Color.CORNFLOWERBLUE);
        progressCircle.setStroke(Color.DARKBLUE);
        progressCircle.setStrokeWidth(2);
        progressLabel.setTextFill(Color.WHITE);
        progressLabel.setStyle("-fx-font-weight: bold;");
    }

    private void setupCountdownTimer() {
        timerLabel = new Label("05:00");
        timerLabel.getStyleClass().add("timer-label");
        HBox titleBox = (HBox) ((VBox) answersContainer.getParent()).getChildren().get(0);
        titleBox.getChildren().add(timerLabel);

        timer = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            remainingTimeSeconds--;
            updateTimerDisplay();
            if (remainingTimeSeconds <= 0) {
                timer.stop();
                showResults();
            }
            if (remainingTimeSeconds <= 60) {
                timerLabel.setStyle(remainingTimeSeconds % 2 == 0 ?
                        "-fx-background-color: #FF3333;" : "-fx-background-color: #3A3A3A;");
            }
        }));
        timer.setCycleCount(Timeline.INDEFINITE);
        timer.play();
    }

    private void updateTimerDisplay() {
        timerLabel.setText(String.format("%02d:%02d", remainingTimeSeconds / 60, remainingTimeSeconds % 60));
    }

    private void loadQuestions() {
        questions = questionService.getQuestions();
        Collections.shuffle(questions);
    }

    private void showQuestion() {
        answersContainer.getChildren().clear();
        Question current = questions.get(currentQuestionIndex);
        questionNumberLabel.setText("Question #" + (currentQuestionIndex + 1));
        questionTextLabel.setText(current.getQuestion());
        setQuestionDifficulty(current);

        ToggleGroup answerGroup = new ToggleGroup();
        addStyledAnswerButton(current.getReponse1(), "A", answerGroup);
        addStyledAnswerButton(current.getReponse2(), "B", answerGroup);
        addStyledAnswerButton(current.getReponse3(), "C", answerGroup);
        addStyledAnswerButton(current.getReponse4(), "D", answerGroup);

        if (userAnswers.containsKey(currentQuestionIndex)) {
            String previousAnswer = userAnswers.get(currentQuestionIndex);
            getRadioButtons().stream()
                    .filter(rb -> rb.getUserData().toString().equals(previousAnswer))
                    .findFirst()
                    .ifPresent(rb -> rb.setSelected(true));
        }
    }

    private void setQuestionDifficulty(Question question) {
        int length = question.getQuestion().length();
        String difficulty = length < 50 ? "Easy" : length < 100 ? "Medium" : "Hard";
        difficultyLabel.setText(difficulty);
        difficultyLabel.setStyle("-fx-background-color: " +
                (length < 50 ? "#4CAF50" : length < 100 ? "#FFA726" : "#F44336") +
                "; -fx-text-fill: white; -fx-padding: 2 8; -fx-background-radius: 4;");
    }

    private List<RadioButton> getRadioButtons() {
        return answersContainer.getChildren().stream()
                .map(node -> (RadioButton) ((HBox) node).getChildren().get(0))
                .toList();
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
        saveCurrentAnswer();
        if (currentQuestionIndex < questions.size() - 1) {
            currentQuestionIndex++;
            showQuestion();
            updateProgress();
        } else {
            showResults();
        }
    }

    @FXML
    private void handlePreviousQuestion() {
        saveCurrentAnswer();
        if (currentQuestionIndex > 0) {
            currentQuestionIndex--;
            showQuestion();
            updateProgress();
        }
    }

    private void saveCurrentAnswer() {
        RadioButton selected = (RadioButton) answersContainer.lookup(".radio-button:selected");
        if (selected != null) {
            userAnswers.put(currentQuestionIndex, selected.getUserData().toString());
            checkAllAnswers();
            updateXPDisplay();
        }
    }

    private void checkAllAnswers() {
        score = 0;
        for (Map.Entry<Integer, String> entry : userAnswers.entrySet()) {
            int questionIndex = entry.getKey();
            String userAnswer = entry.getValue();
            String correctAnswer = questions.get(questionIndex).getReponseCorrecte();

            if (userAnswer.equalsIgnoreCase(correctAnswer)) {
                score++;
            }
        }
        totalXP = score * XP_PER_QUESTION;
    }

    private void updateProgress() {
        progressLabel.setText((currentQuestionIndex + 1) + "/" + questions.size());
    }

    private void updateXPDisplay() {
        int level = (totalXP / 100) + 1;
        int currentLevelXP = totalXP % 100;
        xpProgressBar.setProgress(currentLevelXP / 100.0);
        xpLabel.setText(currentLevelXP + "/100 XP (Level " + level + ")");
    }

    private void showResults() {
        timer.stop();
        checkAllAnswers();

        VBox resultsContent = new VBox(10);
        Label scoreLabel = new Label("Final Score: " + score + "/" + questions.size());
        scoreLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        int minutesTaken = (300 - remainingTimeSeconds) / 60;
        int secondsTaken = (300 - remainingTimeSeconds) % 60;
        Label timeLabel = new Label("Time Used: " + String.format("%02d:%02d", minutesTaken, secondsTaken));
        timeLabel.setStyle("-fx-font-size: 14px;");

        Label xpEarnedLabel = new Label("XP Earned: " + totalXP);
        xpEarnedLabel.setStyle("-fx-font-size: 14px;");

        resultsContent.getChildren().addAll(scoreLabel, timeLabel, xpEarnedLabel);

        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Quiz Results");
        dialog.getDialogPane().setContent(resultsContent);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.showAndWait();
    }
}