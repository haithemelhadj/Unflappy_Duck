package tn.esprit.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import tn.esprit.models.Question;
import tn.esprit.services.ServiceQuestion;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class QuizController {

    @FXML private TextField questionNumberField;
    @FXML private TextField questionField;
    @FXML private TextField answer1Field;
    @FXML private TextField answer2Field;
    @FXML private TextField answer3Field;
    @FXML private TextField answer4Field;
    @FXML private ComboBox<String> correctAnswerCombo;
    @FXML private ListView<Question> questionListView;
    @FXML private TextField searchField;
    @FXML private ComboBox<String> sortComboBox;

    private final ServiceQuestion serviceQuestion = new ServiceQuestion();

    @FXML
    public void initialize() {
        initializeComponents();
        loadQuestions();
    }

    private void initializeComponents() {
        // Initialize ComboBoxes
        correctAnswerCombo.getItems().addAll("A", "B", "C", "D");
        sortComboBox.getItems().addAll(
                "Default", "Question Number ↑", "Question Number ↓",
                "A-Z Questions", "Z-A Questions"
        );
        sortComboBox.getSelectionModel().selectFirst();

        // Configure ListView
        questionListView.setCellFactory(lv -> new ListCell<Question>() {
            @Override
            protected void updateItem(Question item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : String.format("Q%d: %s", item.getQno(), item.getQuestion()));
            }
        });

        // Add selection listener
        questionListView.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldVal, newVal) -> populateFields(newVal)
        );
    }

    private void populateFields(Question question) {
        if (question != null) {
            questionNumberField.setText(String.valueOf(question.getQno()));
            questionField.setText(question.getQuestion());
            answer1Field.setText(question.getReponse1());
            answer2Field.setText(question.getReponse2());
            answer3Field.setText(question.getReponse3());
            answer4Field.setText(question.getReponse4());
            correctAnswerCombo.setValue(question.getReponseCorrecte());
        }
    }

    @FXML
    private void handleAddQuestion() {
        if (!validateInput()) return;

        Question question = new Question(
                0,
                Integer.parseInt(questionNumberField.getText()),
                questionField.getText(),
                answer1Field.getText(),
                answer2Field.getText(),
                answer3Field.getText(),
                answer4Field.getText(),
                correctAnswerCombo.getValue()
        );

        if (serviceQuestion.addQuestion(question)) {
            loadQuestions();
            clearFields();
        }
    }

    @FXML
    private void handleUpdateQuestion() {
        Question selected = questionListView.getSelectionModel().getSelectedItem();
        if (selected == null || !validateInput()) return;

        selected.setQno(Integer.parseInt(questionNumberField.getText()));
        selected.setQuestion(questionField.getText());
        selected.setReponse1(answer1Field.getText());
        selected.setReponse2(answer2Field.getText());
        selected.setReponse3(answer3Field.getText());
        selected.setReponse4(answer4Field.getText());
        selected.setReponseCorrecte(correctAnswerCombo.getValue());

        serviceQuestion.updateQuestion(selected);
        loadQuestions();
    }

    @FXML
    private void handleDeleteQuestion() {
        Question selected = questionListView.getSelectionModel().getSelectedItem();
        if (selected == null) return;

        serviceQuestion.deleteQuestion(selected.getQid());
        loadQuestions();
    }

    @FXML
    private void handleSearch() {
        String keyword = searchField.getText().toLowerCase();
        List<Question> allQuestions = serviceQuestion.getQuestions(); // Get all questions
        List<Question> filtered = allQuestions.stream()
                .filter(q -> q.getQuestion().toLowerCase().contains(keyword)
                        || q.getReponse1().toLowerCase().contains(keyword)
                        || q.getReponse2().toLowerCase().contains(keyword)
                        || q.getReponse3().toLowerCase().contains(keyword)
                        || q.getReponse4().toLowerCase().contains(keyword)
                        || q.getReponseCorrecte().toLowerCase().contains(keyword))
                .collect(Collectors.toList());
        questionListView.getItems().setAll(filtered);
    }

    @FXML
    private void handleSort() {
        switch (sortComboBox.getValue()) {
            case "Default":
                loadQuestions(); // Reload original order
                break;
            case "Question Number ↑":
                questionListView.getItems().sort(Comparator.comparingInt(Question::getQno));
                break;
            case "Question Number ↓":
                questionListView.getItems().sort((q1, q2) -> q2.getQno() - q1.getQno());
                break;
            case "A-Z Questions":
                questionListView.getItems().sort(Comparator.comparing(Question::getQuestion));
                break;
            case "Z-A Questions":
                questionListView.getItems().sort((q1, q2) -> q2.getQuestion().compareTo(q1.getQuestion()));
                break;
        }
        questionListView.refresh();
    }

    @FXML
    private void handleStartQuiz() {
        try {
            Stage stage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("/FrontOffice/GestionCours/quiz.fxml"));
            stage.setScene(new Scene(root));
            stage.setTitle("Quiz Session");
            stage.show();
        } catch (IOException e) {
            showAlert("Error", "Could not load quiz interface");
        }
    }

    private boolean validateInput() {
        if (questionNumberField.getText().isEmpty() ||
                questionField.getText().isEmpty() ||
                answer1Field.getText().isEmpty() ||
                answer2Field.getText().isEmpty() ||
                answer3Field.getText().isEmpty() ||
                answer4Field.getText().isEmpty() ||
                correctAnswerCombo.getValue() == null) {

            showAlert("Error", "All fields are required!");
            return false;
        }
        return true;
    }

    private void loadQuestions() {
        questionListView.getItems().setAll(serviceQuestion.getQuestions());
    }

    private void clearFields() {
        questionNumberField.clear();
        questionField.clear();
        answer1Field.clear();
        answer2Field.clear();
        answer3Field.clear();
        answer4Field.clear();
        correctAnswerCombo.setValue(null);
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}