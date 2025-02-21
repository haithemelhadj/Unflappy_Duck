package tn.esprit.test;

import tn.esprit.models.Question;
import tn.esprit.services.ServiceQuestion;
import java.util.List;
import java.util.Scanner;

public class CrudTest {
    private static final ServiceQuestion questionDAO = new ServiceQuestion();
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        while (true) {
            displayMenu();
            int choice = getIntInput("Enter your choice: ");

            switch (choice) {
                case 1 -> addQuestion();
                case 2 -> listQuestions();
                case 3 -> updateQuestion();
                case 4 -> deleteQuestion();
                case 5 -> System.exit(0);
                default -> System.out.println("Invalid choice!");
            }
        }
    }

    private static void displayMenu() {
        System.out.println("\n==== Quiz CRUD Test ====");
        System.out.println("1. Add a question");
        System.out.println("2. List questions");
        System.out.println("3. Update a question");
        System.out.println("4. Delete a question");
        System.out.println("5. Exit");
    }

    private static void addQuestion() {
        System.out.println("\n=== Add a New Question ===");
        int number = getIntInput("Enter the question number: ");
        String text = getStringInput("Enter the question text: ");
        String ans1 = getStringInput("Enter answer 1: ");
        String ans2 = getStringInput("Enter answer 2: ");
        String ans3 = getStringInput("Enter answer 3: ");
        String ans4 = getStringInput("Enter answer 4: ");
        String correctAnswer = getStringInput("Enter the correct answer (A/B/C/D): ");

        // Construct a new question: id is 0 (auto-generated), followed by number, text, ans1, ans2, ans3, ans4, correctAnswer.
        Question newQuestion = new Question(0, number, text, ans1, ans2, ans3, ans4, correctAnswer);

        if (questionDAO.addQuestion(newQuestion)) {
            System.out.println("Question added successfully!");
            listQuestions();
        } else {
            System.out.println("Failed to add the question!");
        }
    }

    private static void updateQuestion() {
        listQuestions();
        int id = getIntInput("Enter the ID of the question to update: ");

        Question question = findQuestionById(id);
        if (question == null) return;

        System.out.println("Leave blank to keep the current value");
        String newText = getStringInput("New text for the question [" + question.getQuestion() + "]: ");
        if (!newText.isEmpty()) {
            question.setQuestion(newText);
        }
        // Update other fields similarly if needed...

        if (questionDAO.updateQuestion(question)) {
            System.out.println("Question updated successfully!");
            listQuestions();
        } else {
            System.out.println("Failed to update the question!");
        }
    }

    private static void deleteQuestion() {
        listQuestions();
        int id = getIntInput("Enter the ID of the question to delete: ");

        if (questionDAO.deleteQuestion(id)) {
            System.out.println("Question deleted successfully!");
            listQuestions();
        } else {
            System.out.println("Failed to delete the question!");
        }
    }

    private static void listQuestions() {
        System.out.println("\n=== Current Questions ===");
        List<Question> questions = questionDAO.getQuestions();
        if (questions.isEmpty()) {
            System.out.println("No questions found!");
            return;
        }

        for (Question q : questions) {
            System.out.printf("[ID: %d] Q%d: %s%n", q.getQid(), q.getQno(), q.getQuestion());
            System.out.printf("A) %s%nB) %s%nC) %s%nD) %s%n",
                    q.getReponse1(), q.getReponse2(), q.getReponse3(), q.getReponse4());
            System.out.println("Correct answer: " + q.getReponseCorrecte() + "\n");
        }
    }

    private static Question findQuestionById(int id) {
        return questionDAO.getQuestions().stream()
                .filter(q -> q.getQid() == id)
                .findFirst()
                .orElseGet(() -> {
                    System.out.println("Question not found!");
                    return null;
                });
    }

    private static int getIntInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number!");
            }
        }
    }

    private static String getStringInput(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }
}
