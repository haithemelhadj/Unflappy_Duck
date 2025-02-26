package tn.esprit.services;

import tn.esprit.interfaces.IService;
import tn.esprit.models.Question;
import tn.esprit.utils.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceQuestion implements IService<Question> {


    public List<Question> getQuestions() {
        List<Question> questions = new ArrayList<>();
        String query = "SELECT * FROM questions";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Question q = new Question(
                        rs.getInt("qid"),
                        rs.getInt("qno"),
                        rs.getString("question"),
                        rs.getString("reponse1"),
                        rs.getString("reponse2"),
                        rs.getString("reponse3"),
                        rs.getString("reponse4"),
                        rs.getString("reponse_correcte")
                );
                questions.add(q);
            }
        } catch (SQLException e) {
            System.err.println("Error fetching questions: " + e.getMessage());
        }
        return questions;
    }

    public boolean addQuestion(Question question) {
        String query = "INSERT INTO questions (qno, question, reponse1, reponse2, reponse3, reponse4, reponse_correcte) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, question.getQno());
            stmt.setString(2, question.getQuestion());
            stmt.setString(3, question.getReponse1());
            stmt.setString(4, question.getReponse2());
            stmt.setString(5, question.getReponse3());
            stmt.setString(6, question.getReponse4());
            stmt.setString(7, question.getReponseCorrecte());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error adding question: " + e.getMessage());
            return false;
        }
    }

    public boolean updateQuestion(Question question) {
        String query = "UPDATE questions SET qno = ?, question = ?, reponse1 = ?, reponse2 = ?, reponse3 = ?, reponse4 = ?, reponse_correcte = ? WHERE qid = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, question.getQno());
            stmt.setString(2, question.getQuestion());
            stmt.setString(3, question.getReponse1());
            stmt.setString(4, question.getReponse2());
            stmt.setString(5, question.getReponse3());
            stmt.setString(6, question.getReponse4());
            stmt.setString(7, question.getReponseCorrecte());
            stmt.setInt(8, question.getQid());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating question: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteQuestion(int qid) {
        String query = "DELETE FROM questions WHERE qid = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, qid);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting question: " + e.getMessage());
            return false;
        }
    }

    // IService
    @Override
    public void add(Question question) {
        if (!addQuestion(question)) {
            throw new RuntimeException("Failed to add question");
        }
    }

    @Override
    public List<Question> getAll() {
        return getQuestions();
    }

    @Override
    public void update(Question question) {
        if (!updateQuestion(question)) {
            throw new RuntimeException("Failed to update question");
        }
    }

    @Override
    public void delete(Question question) {
        if (!deleteQuestion(question.getQid())) {
            throw new RuntimeException("Failed to delete question");
        }
    }

    public List<Question> searchQuestions(String keyword) {
        return List.of();
    }
}
