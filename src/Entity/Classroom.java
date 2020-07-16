package Entity;

public class Classroom {
	public int id;
	public int volume;
	public int remainVolume;
	public String type;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getVolume() {
		return volume;
	}
	public void setVolume(int volume) {
		this.volume = volume;
	}
	public int getRemainVolume() {
		return remainVolume;
	}
	public void setRemainVolume(int remainVolume) {
		this.remainVolume = remainVolume;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
}
