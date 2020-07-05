package Entity;

public class Course {
	public String name;
	public String type;
	public int teacherId;
	public int classroomId;
	public byte terms;
	public byte periods;
	public int weektime;
	public int volume;
	public int remainVolume;
	public float point;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public int getTeacherId() {
		return teacherId;
	}
	public void setTeacherId(int teacherId) {
		this.teacherId = teacherId;
	}
	public int getClassroomId() {
		return classroomId;
	}
	public void setClassroomId(int classroomId) {
		this.classroomId = classroomId;
	}
	public byte getTerms() {
		return terms;
	}
	public void setTerms(byte terms) {
		this.terms = terms;
	}
	public byte getPeriods() {
		return periods;
	}
	public void setPeriods(byte periods) {
		this.periods = periods;
	}
	public int getWeektime() {
		return weektime;
	}
	public void setWeektime(int weektime) {
		this.weektime = weektime;
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
	public float getPoint() {
		return point;
	}
	public void setPoint(float point) {
		this.point = point;
	}
}
