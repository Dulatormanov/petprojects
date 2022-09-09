```
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class Quiz {
    private String name;
    ArrayList<Question> questions;
    private int cnt = 0;

    public Quiz() {
        questions = new ArrayList<>();
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void addQuestion(Question question) {
        questions.add(question);
    }

    public static Quiz loadFromFile(String fileName) {
        Quiz q = new Quiz();
        int cntLine = 0;
        q.setName(fileName.substring(0, fileName.length() - 4));
        try {
            BufferedReader r = new BufferedReader(new FileReader(fileName));
            String st;
            ArrayList<ArrayList<String>> qs = new ArrayList<>();
            ArrayList<String> tmp = new ArrayList<>();

            while ((st = r.readLine()) != null) {
                if (st.isEmpty()) {
                    qs.add(tmp);
                    tmp = new ArrayList<>();
                } else {
                    tmp.add(st);
                }
                cntLine++;
            }

            if (cntLine == 0) {
                throw new InvalidQuizFormatException("Invalid Quiz txt");
            }

            qs.add(tmp);

            for (ArrayList<String> arr: qs) {
                if (arr.size() == 2) {
                    FillIn fill = new FillIn();
                    fill.setDescription(arr.get(0));
                    fill.setAnswer(arr.get(1));
                    q.addQuestion(fill);
                } else {
                    Test test = new Test();
                    test.setDescription(arr.get(0));
                    test.setAnswer(arr.get(1));
                    arr.remove(0);
                    Collections.shuffle(arr);
                    String[] ch = new String[arr.size()];
                    test.setOptions(arr.toArray(ch));
                    q.addQuestion(test);
                }
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            System.exit(0);
        }

        return q;
    }

    public String toString() {
        return "Welcome TO \"" + name + "\" QUIZ!";
    }

    public void start() {
        Scanner scanner = new Scanner(System.in);
        boolean isTest = false;
        for (int i = 0; i < questions.size(); i++) {
            System.out.println(i + 1 + "." + questions.get(i));

            if (questions.get(i) instanceof Test) {
                String[] options = ((Test)(questions.get(i))).getOptions();
                for (int k = 0; k < options.length; k++) {
                    System.out.println(Character.toString((char)(65 + k)) + " " + options[k]);
                }
                isTest = true;
            }

            String answer = scanner.next();

//            if (isTest && (answer.length() == 1) && (answer.charAt(0) > 65 || answer.charAt(0) > 90)) {
//                System.out.println("Invalid Choice! Try again (Ex: A, B, ...)");
//                i--;
//                continue;
//            }

            while (isTest && (answer.length() != 1 || (answer.charAt(0) < 65 || answer.charAt(0) > 90))) {
                System.out.println("Invalid Choice! Try again (Ex: A, B, ...)");
                answer = scanner.next();
            }
            isTest = false;

            if (answer.equals(questions.get(i).getAnswer())) {
                System.out.println("Correct!");
                cnt++;
            } else {
                System.out.println("Incorrect!");
            }

        }
        System.out.println("Correct Answers: " + cnt + "/" + questions.size() + " " + ((double) cnt / questions.size() * 100) + "%");
    }
}


class InvalidQuizFormatException extends FileNotFoundException {
    public InvalidQuizFormatException(String s) {
        super(s);
    }
}
public class FillIn extends Question {

    public String toString() {
        return getDescription();
    }
}
import java.io.BufferedReader;
import java.io.FileReader;

abstract public class Question {
    private String description;
    private String answer;

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getAnswer() {
        return answer;
    }

}

public class QuizMaker {
    public static void main(String[] args) throws Exception {
        Quiz q = Quiz.loadFromFile(args[0]);
        System.out.println(q);
        q.start();
    }
}

import java.util.ArrayList;

class Test extends Question {
    private String[] options;
    private int numOfOptions;
    private ArrayList<String> labels;

    public Test() {
        numOfOptions = 4;
        labels = new ArrayList<>();
        for (int i = 0; i < numOfOptions; i++) {
            labels.add(Character.toString(((char)(65 + i))));
        }
        labels.indexOf("A");
    }

    public void setOptions(String[] options) {
        for (int i = 0; i < options.length; i++) {
            if (getAnswer().equals(options[i])) {
                setAnswer(Character.toString((char)(65 + i)));
            }
        }
        this.options = options;
    }

    public String[] getOptions() {
        return options;
    }

    public String toString() {
        return getDescription();
    }
}
```
