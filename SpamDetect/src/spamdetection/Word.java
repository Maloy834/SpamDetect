package spamdetection;
public class Word {
	private String word;	
	private int spamCount;	
	private int hamCount;	
	private float spamRate;	
	private float hamRate;
	private float probOfSpam;
	private float probOfHam;
	
	public Word(String word){
		this.word = word;
		spamCount = 0;
		hamCount = 0;
		spamRate = 0.0f;
		hamRate = 0.0f;
		probOfSpam = 0.0f;
		probOfHam=0.0f;
	}
	
	public void countSpam(){
		spamCount++;
	}
	
	public void countHam(){
		hamCount++;
	}
	
	
	public void calculateProbability(int totSpam, int totHam){
		spamRate = spamCount / (float) totSpam;
		hamRate = hamCount / (float) totHam;
		
		if(spamRate + hamRate > 0){
			probOfSpam = spamRate / (spamRate + hamRate);
			probOfHam=hamRate/(spamRate+hamRate);
		}
		if(probOfSpam < 0.01f){
			probOfSpam = 0.01f;
			
		}
		if(probOfSpam > 0.99){
			probOfSpam = 0.99f;
		}
		if(probOfHam < 0.01f){
			probOfHam = 0.01f;
			
		}
		if(probOfHam > 0.99){
			probOfHam = 0.99f;
		}
		
	}

	public String getWord() {
		return word;
	}

	public float getSpamRate() {
		return spamRate;
	}

	public float getHamRate() {
		return hamRate;
	}

	public void setHamRate(float hamRate) {
		this.hamRate = hamRate;
	}

	public float getProbOfSpam() {
		return probOfSpam;
	}

	public void setProbOfSpam(float probOfSpam) {
		this.probOfSpam = probOfSpam;
	}
	public float getProbOfHam() {
		return probOfHam;
	}

	public void setProbOfHam(float probOfHam) {
		this.probOfHam = probOfHam;
	}
}
