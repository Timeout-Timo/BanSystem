package de.timeout.ban.reason;

public class Reason {

	protected String name, prefix;
	protected long firstStage, secondStage, thirdStage;
	protected ReasonType type;
	protected int points, firstLine, secondLine;
	
	public Reason(String name) {
		this.name = name;
	}
	
	public Reason(String name, String prefix, ReasonType type, long firstStage, long secondStage, long thirdStage, int firstLine, int secondLine, int points) {
		this.name = name;
		this.prefix = prefix;
		this.type = type;
		this.firstStage = firstStage;
		this.secondStage = secondStage;
		this.thirdStage = thirdStage;
		this.firstLine = firstLine;
		this.secondLine = secondLine;
		this.points = points;
	}
	
	public long getFirstStage() {
		return firstStage;
	}

	public void setFirstStage(long firstStage) {
		this.firstStage = firstStage;
	}

	public long getSecondStage() {
		return secondStage;
	}

	public void setSecondStage(long secondStage) {
		this.secondStage = secondStage;
	}

	public long getThirdStage() {
		return thirdStage;
	}

	public void setThirdStage(long thirdStage) {
		this.thirdStage = thirdStage;
	}

	public int getPoints() {
		return points;
	}

	public void setPoints(int points) {
		this.points = points;
	}

	public int getFirstLine() {
		return firstLine;
	}

	public void setFirstLine(int firstLine) {
		this.firstLine = firstLine;
	}

	public int getSecondLine() {
		return secondLine;
	}

	public void setSecondLine(int secondLine) {
		this.secondLine = secondLine;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public ReasonType getType() {
		return type;
	}

	public void setType(ReasonType type) {
		this.type = type;
	}

	public enum ReasonType {
		MUTE, BAN;
	}
}
