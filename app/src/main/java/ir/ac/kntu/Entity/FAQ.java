package ir.ac.kntu.Entity;

import com.google.gson.annotations.SerializedName;

public class FAQ {
    @SerializedName("question")
    private String question;
    @SerializedName("answer")
    private String answer;

    public FAQ(String question, String answer) {
        this.question = question;
        this.answer = answer;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
