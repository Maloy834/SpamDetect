package spamdetection;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class Bayes {
	HashMap<String, Word> wordStorage = new HashMap<String, Word>();
	BufferedWriter out;
	ArrayList<String> arr = new ArrayList<>();
    ArrayList<String>ans=new ArrayList<>();
    ArrayList<String>testFile=new ArrayList<>();
	public void createTestfile(int totalHamCount, int totalSpamCount) throws IOException {

		String filename = "H:\\workspace\\SpamDetect\\src\\spamdetection\\test.txt";
		BufferedWriter bw = null;
		FileWriter fw = null;
		try {
			fw = new FileWriter(filename);
			bw = new BufferedWriter(fw);
			int needham = 10 * totalHamCount / 100;
			int needspam = 10 * totalSpamCount / 100;
			System.out.println("fileHam:" + needham + " " + "filespam:" + needspam);
			while (true) {
				Random rand = new Random();
				int index = rand.nextInt(arr.size());
				String line = arr.get(index);
				String type = line.split("\t")[0];
				if (type.equals("ham") && needham != 0) {
					bw.write(line + "\n");
					arr.remove(index);
					needham--;
				} else if (type.endsWith("spam") && needspam != 0) {
					bw.write(line + "\n");
					arr.remove(index);
					needspam--;
				}
				if (needham == 0 && needspam == 0)
					break;

			}
			
			bw.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		TrainingData();
		TestFile(filename);
		calculateAccuracy();
		
	}
	public void TrainingData() {
		int hamcount = 0;
		int spamcount = 0;
		for (int i = 0; i < arr.size(); i++) {
			String line = arr.get(i);
			String type = line.split("\t")[0];
			String sms = line.split("\t")[1];
			for (String word : sms.split(" ")) {
				word = word.replaceAll("\\W", "");
				word = word.toLowerCase();
				Word w = null;
				if (wordStorage.containsKey(word)) {
					w = (Word) wordStorage.get(word);
				} else {
					w = new Word(word);
					wordStorage.put(word, w);
				}
				if (type.equals("ham")) {
					w.countHam();
					hamcount++;
				} else if (type.equals("spam")) {
					w.countSpam();
					spamcount++;
				}
			}

		}
		for (String key : wordStorage.keySet()) {
			wordStorage.get(key).calculateProbability(spamcount, hamcount);
		}
	}

	public void countHamAndSpam(String input) throws IOException {
		int totalSpamCount = 0;
		int totalHamCount = 0;
		System.out.println(input);
		BufferedReader in = new BufferedReader(new FileReader(input));
		String line = in.readLine();
		while (line != null) {
			if (!line.equals("")) {
				arr.add(line);
				String type = line.split("\t")[0];

				// String sms = line.split("\t")[1];
				if (type.equals("ham")) {
					totalHamCount++;
				} else if (type.equals("spam")) {
					totalSpamCount++;
				}
				
			}
			line = in.readLine();
		}
		in.close();
		System.out.println("Ham: " + totalHamCount + " " + "Spam: " + totalSpamCount);
		createTestfile(totalHamCount, totalSpamCount);
		
	}

	
	public void TestFile(String inputFile) throws IOException {
		BufferedReader in = new BufferedReader(new FileReader(inputFile));
		this.out = new BufferedWriter(new FileWriter("H:\\workspace\\SpamDetect\\src\\spamdetection\\answer.txt"));
		String line = in.readLine();
		while (line != null) {
			if (!line.equals("")) {
				String type = line.split("\t")[0];
				testFile.add(type);
				String sms = line.split("\t")[1];
				ArrayList<Word> wsms = makeWordList(sms);
				boolean isSpam = calculateBayes(wsms);
				if (isSpam == true){
					this.out.write("spam");
					ans.add("spam");
				}
				else if (isSpam == false){
					this.out.write("ham");
					ans.add("ham");
				}
			}
			this.out.newLine();
			line = in.readLine();
		}
		this.out.close();
		in.close();
	}

	// make an arraylist of all words in an sms, set probability of spam to 0.4
	// if word is not known
	public ArrayList<Word> makeWordList(String sms) {
		ArrayList<Word> wordList = new ArrayList<Word>();
		for (String word : sms.split(" ")) {
			word = word.replaceAll("\\W", "");
			word = word.toLowerCase();
			Word w = null;
			if (wordStorage.containsKey(word)) {
				w = (Word) wordStorage.get(word);
			} else {
				w = new Word(word);
				w.setProbOfSpam(0.0f);
				w.setProbOfHam(1.0f);
			}
			wordList.add(w);
		}
		return wordList;
	}

	// Applying Bayes rule and calculating probability of ham or spam. Return
	// true if spam, false if ham
	public boolean calculateBayes(ArrayList<Word> sms) {
		float probabilityOfSpam = 1.0f;
		float probabilityOfHam = 1.0f;
		for (int i = 0; i < sms.size(); i++) {
			Word word = (Word) sms.get(i);
			if(word.getProbOfSpam()!=0)
			   probabilityOfSpam *= word.getProbOfSpam();
			probabilityOfHam *= word.getProbOfHam();
		}
		//System.out.println(probabilityOfSpam+" "+probabilityOfHam);
		//float probOfSpam = probabilityOfSpam / (probabilityOfSpam + probabilityOfHam);
		if (probabilityOfSpam>probabilityOfHam)
			return true;
		else
			return false;
	}
	public void calculateAccuracy() throws IOException{
		String pos="ham";
		String neg="spam";
		double tp=0,fp=0,fn=0,tn=0;
		double accuracy,precision,recall,f_measure;
		for(int i=0;i<testFile.size();i++)
		{
			if(testFile.get(i).equals("ham")&& ans.get(i).equals("ham"))
				tp++;
			else if(testFile.get(i).equals("ham")&& ans.get(i).equals("spam"))
				fn++;
			else if(testFile.get(i).equals("spam")&& ans.get(i).equals("ham"))
				fp++;
			else if(testFile.get(i).equals("spam")&& ans.get(i).equals("spam"));
				tn++;
			
		}
		
		System.out.println("True positive:"+tp+" "+"False negative:"+fn+" "+"False positive:"+fp+" "+"True negative:"+tn);
		/*accuracy=((double)tp+(double)tn)/((double)tp+(double)tn+(double)fn+(double)fp);
		precision=(double)tp/((double)tp+(double)fp);
		recall=(double)tp/((double)tp+(double)fn);
		f_measure=(2*precision*recall)/(precision+recall);*/
		accuracy=(tp+tn)/(tp+tn+fn+fp);
		precision=tp/(tp+fp);
		recall=tp/(tp+fn);
		f_measure=(2*precision*recall)/(precision+recall);
		System.out.println("Accuracy:"+accuracy+" "+"Precision:"+precision+" "+"Recall:"+recall+" "+"F_measure:"+f_measure);
		
	}
}
