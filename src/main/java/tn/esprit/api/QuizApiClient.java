package tn.esprit.api;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import tn.esprit.models.Question;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class QuizApiClient {

    // API endpoint to fetch 10 multiple-choice trivia questions
    private static final String API_URL = "https://opentdb.com/api.php?amount=10&type=multiple";

    public List<Question> fetchQuestions() throws Exception {
        URL url = new URL(API_URL);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
            throw new RuntimeException("HTTP error code: " + connection.getResponseCode());
        }

        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder responseBuilder = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            responseBuilder.append(line);
        }
        reader.close();

        // Parse JSON response using Gson
        Gson gson = new Gson();
        QuizApiResponse apiResponse = gson.fromJson(responseBuilder.toString(), QuizApiResponse.class);

        List<Question> questions = new ArrayList<>();
        // Map each API question to your Question model
        for (QuizQuestion apiQuestion : apiResponse.results) {
            Question q = new Question();
            q.setQuestion(apiQuestion.question);
            // Combine correct and incorrect answers
            List<String> answers = new ArrayList<>();
            answers.add(apiQuestion.correct_answer);
            answers.addAll(apiQuestion.incorrect_answers);
            // Shuffle answers to randomize order
            Collections.shuffle(answers);
            if (answers.size() >= 4) {
                q.setReponse1(answers.get(0));
                q.setReponse2(answers.get(1));
                q.setReponse3(answers.get(2));
                q.setReponse4(answers.get(3));
            }
            // Store the correct answer separately
            q.setReponseCorrecte(apiQuestion.correct_answer);
            // Optionally, set the question number later (e.g., when inserting into the database)
            questions.add(q);
        }
        return questions;
    }

    // Helper classes to map the JSON structure
    public static class QuizApiResponse {
        @SerializedName("response_code")
        public int responseCode;
        public List<QuizQuestion> results;
    }

    public static class QuizQuestion {
        public String category;
        public String type;
        public String difficulty;
        public String question;
        @SerializedName("correct_answer")
        public String correct_answer;
        @SerializedName("incorrect_answers")
        public List<String> incorrect_answers;
    }
}
