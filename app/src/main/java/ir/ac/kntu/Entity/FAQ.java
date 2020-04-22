package ir.ac.kntu.Entity;

import com.google.gson.annotations.SerializedName;

public class FAQ {
    @SerializedName("question")
    private String question;
    @SerializedName("answer")
    private String answer;
    @SerializedName("image")
    private String image;
    @SerializedName("shown")
    private int shown;
    @SerializedName("priority")
    private int priority;


    public FAQ(String question, String answer, String image, int shown, int priority) {
        this.question = question;
        this.answer = answer;
        this.image = image;
        this.shown = shown;
        this.priority = priority;
    }

    public FAQ(String question, String answer, String image) {
        this.question = question;
        this.answer = answer;
        this.image = image;
        this.priority = 1;
        this.shown = 1;
    }

    public FAQ(String question, String answer) {
        this.question = question;
        this.answer = answer;
        this.priority = 1;
        this.shown = 1;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getShown() {
        return shown;
    }

    public void setShown(int shown) {
        this.shown = shown;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }
}
