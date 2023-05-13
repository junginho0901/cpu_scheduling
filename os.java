package src;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

class process {
	int ID;// 프로세스 id
	int arrival;// 도착시간
	int service;// 실행시간
	int priority;// 우선순위
	int waiting;// 대기시간
	int save;// 남은작업시간
	int response;// 응답시간=실행후로부터+2
	int count = 0;// 응답시간 시점을 확인하기 위한 변수

	int[] saveArr = new int[8];

	public process(String iD, int arrival, int service, int priority, int waiting, int save, int response) {
		super();
		ID = Integer.parseInt(iD.replaceAll("[^0-9]", ""));// id에서 숫자만 받아오기
		this.arrival = arrival;
		this.service = service;
		this.priority = priority;
		this.waiting = -arrival;
		this.save = service;
		this.response = -arrival;
		saveArr[0] = arrival;// 파일에서 받아온 내용을 저장하여 추후에 리셋을 할 때 사용
		saveArr[1] = service;
		saveArr[2] = priority;
		saveArr[3] = -arrival;
		saveArr[4] = service;
		saveArr[5] = -arrival;
		saveArr[6] = ID;
		saveArr[7] = 0;

	}

	public void reset() {// 다른 스케줄링을 돌리기 전에 초기 상태로 리셋

		this.arrival = saveArr[0];
		this.service = saveArr[1];
		this.priority = saveArr[2];
		this.waiting = saveArr[3];
		this.save = saveArr[4];
		this.response = saveArr[5];
		this.count = 0;
	}

	public int getID() {// 각각의 변수의 get set함수들
		return ID;
	}

	public void setID(String iD) {
		ID = Integer.parseInt(iD.replaceAll("[^0-9]", ""));
	}

	public int getArrival() {
		return arrival;
	}

	public void setArrival(int arrival) {
		this.arrival = arrival;
	}

	public int getService() {
		return service;
	}

	public void setService(int service) {
		this.service = service;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public int getWaiting() {
		return waiting;
	}

	public void setWaiting(int waiting) {
		this.waiting = waiting;
	}

	public int getSave() {
		return save;
	}

	public void setSave(int save) {
		this.save = save;
	}

	public int getResponse() {
		return response;
	}

	public void setResponse(int response) {
		this.response = response;
	}

	public void plusWait() {// 대기 시간을 증가, 만약 응답이 아직 안나왔다면 응답 시간도 증가.
		waiting++;
		if (count != 2) {
			response++;
		}

	}

	public void justwait() {// 대기 시간만 증가
		waiting++;
	}

	public void running() {// 프로세스가 cpu를 차지하고 있는 상황
		if (save != 0) // 작업양이 남아 있다면 작업량을 줄이고
			save--;
		if (count != 2) {// 아직 응답이 나오지 않았다면 응답시간도 증가
			response++;
			count++;
		}
	}

}

class scheduling {// 각종 스케줄링 저장 클래스
	process[] pro;// 프로세스 정보 저장

	int timeq;
	int[] time = new int[10000];// 간트차트

	public scheduling(process[] pro, int timeq) {// 생성자
		super();
		this.pro = pro;
		this.timeq = timeq;
	}

	public void reset() {// 프로세스들의 상태를 초기 상태로 리셋하는 함수
		for (int i = 0; i < pro.length; i++) {
			pro[i].reset();
		}
	}

	public void print() {// 스케줄링 간트차트 및 각 정보들 출력
		double AWT = 0;// 평균 대기 시간
		double ART = 0;// 평균 응답 시간
		double ATT = 0;// 평균 반환 시간
		// 전체 프로세스의 모든 작업량을 다 수행했는지 확인하는 배열
		Arrays.sort(pro, (a, b) -> a.ID - b.ID);
		
		System.out.print("| ");
		for (int i = 0; i < 10000; i++) {// 간트차트 출력
			if (time[i] == -1)
				break;
			System.out.print(time[i]);
			
			if (time[i] != time[i + 1]) {
				System.out.print(" | ");

			}
		}
		System.out.println();
		for (int i = 0; i < pro.length; i++) {// 각 프로세스의 대기시간 응답시간 반환시간
			System.out.println("프로세스" + pro[i].ID + " 대기시간:" + pro[i].getWaiting());
			System.out.println("프로세스" + pro[i].ID + " 응답시간:" + pro[i].getResponse());
			System.out.println("프로세스" + pro[i].ID + " 반환시간:" + (pro[i].getWaiting() + pro[i].getService()));
			System.out.println();

			AWT += pro[i].getWaiting();
			ART += pro[i].getResponse();
			ATT += pro[i].getWaiting() + pro[i].getService();
		}

		System.out.println();
		System.out.println("평균대기시간: " + AWT / pro.length);// 평균 대기, 평균 응답, 평균 반환 출력
		System.out.println("평균응답시간: " + ART / pro.length);
		System.out.println("평균반환시간: " + ATT / pro.length);

	}

	public void Timesliceprint() {// 타임슬라이스를 사용하는 스케줄링을 출력
		double AWT = 0;
		double ART = 0;
		double ATT = 0;
		
		int count = 0;
		Arrays.sort(pro, (a, b) -> a.ID - b.ID);
		System.out.print("| ");
		for (int i = 0; i < 10000; i++) {
			if (time[i] == -1)
				break;
			System.out.print(time[i]);
			
			count++;
			if (time[i] != time[i + 1]) {
				System.out.print(" | ");
				count = 0;

			}

			if (count == timeq) {
				System.out.print(" | ");
				count = 0;
			}

		}
		System.out.println();

		for (int i = 0; i < pro.length; i++) {
			System.out.println("프로세스" + pro[i].ID + " 대기시간:" + pro[i].getWaiting());
			System.out.println("프로세스" + pro[i].ID + " 응답시간:" + pro[i].getResponse());
			System.out.println("프로세스" + pro[i].ID + " 반환시간:" + (pro[i].getWaiting() + pro[i].getService()));
			System.out.println();

			AWT += pro[i].getWaiting();
			ART += pro[i].getResponse();
			ATT += pro[i].getWaiting() + pro[i].getService();
		}
		System.out.println();
		System.out.println("평균대기시간: " + AWT / pro.length);
		System.out.println("평균응답시간: " + ART / pro.length);
		System.out.println("평균반환시간: " + ATT / pro.length);

	}

	process tmp;

	public void FCFS() {// First Come First Served
		reset();// 프로세스 상태 리셋
		Arrays.fill(time, -1);// 간트차트 초기화

		Arrays.sort(pro, (a, b) -> a.arrival - b.arrival);
		
		int index = 0;// 인덱스
		int currentID = pro[index].ID;// 현재작업중인 프로세스 아이디
		int time1 = 0;// 현재 시간
		while (true) {
			int done = 0;
			for (int i = 0; i < pro.length; i++) {
				if (pro[i].save == 0) {
					done++;
				}
			}
			if (done == pro.length) {
				break;
			}

			for (int i = 0; i < pro.length; i++) {
				if (pro[i].getSave() == 0) {

					continue;
				}
				if (currentID == pro[i].ID) {

					pro[i].running();
					time[time1++] = pro[i].ID;
				} else {
					if (pro[i].count < 2) {// 반응시간이 아직 나오지 않았다면 반응시간도 추가하는 함수 실행
						pro[i].plusWait();

					} else {
						pro[i].justwait();

					}
				}
			}

			for (int i = 0; i < pro.length; i++) {
				if (pro[i].save != 0 && pro[i].waiting >= 0) {// 현재 하던 작업이 끝났다면 다음 작업으로
					currentID = pro[i].ID;
					break;
				}
			}

		}
		System.out.println("FCFS");
		print();
	}

	public void SJF() {// shortest job first
		reset();
		Arrays.fill(time, -1);
		int time1 = 0;
		int index;
		Arrays.sort(pro, (a, b) -> a.arrival - b.arrival);

		index = 0;// 인덱스
		int currentID = pro[index].ID;// 현재작업중인 프로세스 아이디

		while (true) {
			int done = 0;
			for (int i = 0; i < pro.length; i++) {
				if (pro[i].save == 0) {
					done++;
				}
			}
			if (done == pro.length) {
				break;
			}

			for (int i = 0; i < pro.length; i++) {
				if (pro[i].getSave() == 0) {

					continue;
				}
				if (currentID == pro[i].ID) {

					pro[i].running();
					time[time1++] = pro[i].ID;
				} else {
					if (pro[i].count < 2) {// 반응시간이 아직 나오지 않았다면 반응시간도 추가하는 함수 실행
						pro[i].plusWait();

					} else {
						pro[i].justwait();

					}
				}
			}

			if (pro[index].save == 0) {// 현재 프로세스가 끝나면 다음에 할 작업을 찾는다.
				if (index < pro.length) {
					if (index + 1 >= pro.length) {
						break;
					}
					int memmory = index + 1;
					process tmp = pro[++index];
					for (int i = index; i < pro.length; i++) {

						if (tmp.service > pro[i].service && time1 >= pro[i].arrival) {// 작업량이 작고 현재 시간에 큐에 도착해 있다면 변경
							memmory = i;
							tmp = pro[i];
						}
					}
					tmp = pro[memmory];
					pro[memmory] = pro[index];
					pro[index] = tmp;
					currentID = pro[index].ID;
				}

			}

		}
		System.out.println("SJF");
		print();
	}

	public void priority1() {// 비선점 우선순위
		reset();
		Arrays.fill(time, -1);
		int time1 = 0;
		int index;
		Arrays.sort(pro, (a, b) -> a.arrival - b.arrival);

		index = 0;// 인덱스
		int currentID = pro[index].ID;// 현재작업중인 프로세스 아이디

		while (true) {
			int done = 0;
			for (int i = 0; i < pro.length; i++) {
				if (pro[i].save == 0) {
					done++;
				}
			}
			if (done == pro.length) {
				break;
			}

			for (int i = 0; i < pro.length; i++) {
				if (pro[i].getSave() == 0) {
					continue;
				}
				if (currentID == pro[i].ID) {

					pro[i].running();
					time[time1++] = pro[i].ID;
				} else {
					if (pro[i].count < 2) {
						pro[i].plusWait();

					} else {
						pro[i].justwait();

					}
				}
			}

			if (pro[index].save == 0) {
				if (index < pro.length) {
					if (index + 1 >= pro.length) {
						break;
					}

					int memmory = index + 1;
					process tmp = pro[++index];
					for (int i = index; i < pro.length; i++) {
						if (tmp.priority > pro[i].priority && time1 >= pro[i].arrival) {// 우선순위가 먼저고 도착해있다면 다음 작업으로 선택
							memmory = i;
							tmp = pro[i];
						}
					}
					tmp = pro[memmory];
					pro[memmory] = pro[index];
					pro[index] = tmp;
					currentID = pro[index].ID;
				}

			}

		}
		System.out.println("비선점형 우선순위 스케줄링");

		print();
	}

	public void priority2() {// 선점형 우선순위
		reset();
		Arrays.fill(time, -1);
		int time1 = 0;
		int index;
		Arrays.sort(pro, (a, b) -> a.arrival - b.arrival);

		index = 0;// 인덱스
		int currentID = pro[index].ID;// 현재작업중인 프로세스 아이디
		process tmp = pro[index];// 현재작업중인 프로세스
		while (true) {
			int done = 0;
			for (int i = 0; i < pro.length; i++) {
				if (pro[i].save == 0) {
					done++;
				}
			}
			if (done == pro.length) {
				break;
			}

			for (int i = 0; i < pro.length; i++) {
				if (pro[i].getSave() == 0) {
					continue;
				}
				if (currentID == pro[i].ID) {
					pro[i].running();
					time[time1++] = pro[i].ID;
					tmp = pro[i];
					if (tmp.save == 0)// 작업이 끝났다면 우선순위를 매우 크게 바꿔서 프로세스 선택시 영향을 받지 않게 한다.
						tmp.priority = 10000;
				} else {
					if (pro[i].count < 2) {
						pro[i].plusWait();

					} else {
						pro[i].justwait();

					}
				}

			}
			done = 0;
			for (int i = 0; i < pro.length; i++) {
				if (pro[i].save == 0) {

					done++;
				}
			}
			if (done == pro.length) {
				break;
			}

			for (int i = 0; i < pro.length; i++) {// 매 작업 1씩 할 때마다 하고 있는 작업보다 더 우선순위가 먼저인 작업이 있는지 확인
				if (pro[i].priority == 10000)
					continue;
				if (pro[i].save > 0 && pro[i].waiting >= 0 && tmp.priority > pro[i].priority) {// 작업이 끝나지 않고 프로세스가 도착하고
																								// 우선순위가 방금 했던 작업보다
																								// 우선이라면 해당 프로세스로 바꿔준다.
					tmp = pro[i];
					index = i;
					currentID = tmp.ID;
				}

			}
		}
		System.out.println("선점형 우선순위 스케줄링");

		print();
	}

	public void RoundRobin() {// 라운드로빈

		reset();
		Arrays.fill(time, -1);

		int time1 = 0;
		int index;
		Arrays.sort(pro, (a, b) -> a.arrival - b.arrival);

		index = 0;// 인덱스
		int currentID = pro[index].ID;// 현재작업중인 프로세스 아이디
		int timeslice = 0;// 한작업이 타임슬라이스를 2를 넘지 않도록 하기 위한 변수

		process tmp = pro[0];
		while (true) {
			int done = 0;
			for (int i = 0; i < pro.length; i++) {
				if (pro[i].save == 0) {
					done++;
				}
			}
			if (done == pro.length) {
				break;
			}

			for (int i = 0; i < pro.length; i++) {
				if (pro[i].getSave() == 0) {
					continue;
				}
				if (currentID == pro[i].ID) {
					tmp = pro[i];
					pro[i].arrival = time1;
					pro[i].running();
					time[time1++] = pro[i].ID;
					pro[i].arrival = time1;
					timeslice++;// 작업 수행 후 1만큼 증가

				} else {
					if (pro[i].count < 2) {
						pro[i].plusWait();

					} else {
						pro[i].justwait();

					}
				}
			}

			if (pro[index].save == 0 || timeslice == timeq) {// 한 작업이 끝났거나,타임슬라이스를 2만큼 사용했다면 다음 작업을 찾는다.
				done = 0;
				timeslice = 0;
				for (int i = 0; i < pro.length; i++) {
					if (pro[i].save == 0) {
						done++;
					}

				}
				if (done == pro.length) {
					break;
				}
				if (index < pro.length) {
					int min = tmp.arrival;
					for (int i = 0; i < pro.length; i++) {
						if (min > pro[i].arrival && time1 >= pro[i].arrival && pro[i].waiting >= 0
								&& pro[i].save != 0) {
							min = pro[i].arrival;
							index = i;
						}
					}
					currentID = pro[index].ID;
				}
			}
		}
		System.out.println("RoundRobin");
		Timesliceprint();
	}
	
	public void SRT() {

		reset();
		Arrays.fill(time, -1);

		int time1 = 0;
		int index;
		Arrays.sort(pro, (a, b) -> a.arrival - b.arrival);

		index = 0;// 인덱스
		int currentID = pro[index].ID;// 현재작업중인 프로세스 아이디
		int timeslice = 0;

		while (true) {

			int done = 0;
			for (int i = 0; i < pro.length; i++) {
				if (pro[i].save == 0) {
					done++;
				}

			}
			if (done == pro.length) {
				break;
			}

			for (int i = 0; i < pro.length; i++) {
				if (pro[i].getSave() == 0) {
					continue;
				}
				if (currentID == pro[i].ID) {

					pro[i].running();

					time[time1++] = pro[i].ID;
					timeslice++;
				} else {

					if (pro[i].count < 2) {
						pro[i].plusWait();

					} else {

						pro[i].justwait();

					}
				}
			}

			if (pro[index].save == 0 || timeslice == timeq) {
				done = 0;
				timeslice = 0;
				for (int i = 0; i < pro.length; i++) {
					if (pro[i].save == 0) {
						done++;
					}

				}
				if (done == pro.length) {
					break;
				}

				if (index < pro.length) {

					int memmory;
					process tmp;
					while (true) {

						memmory = ++index;
						if (index >= pro.length) {
							index = 0;
							memmory = 0;
						}
						tmp = pro[memmory];

						if (time1 >= tmp.arrival && tmp.save != 0) {
							break;
						}

					}

					for (int i = 0; i < pro.length; i++) {
						if (pro[i].save == 0)
							continue;
						if (tmp.save > pro[i].save && time1 >= pro[i].arrival && pro[memmory].waiting >= 0) {// 작업이 도착해
																												// 있고 남아
																												// 있는
																												// 작업량이
																												// 그 중
																												// 제일
																												// 짧다면
																												// 다음
																												// 작업으로
																												// 정한다.
							memmory = i;
							tmp = pro[i];
						}
					}
					tmp = pro[memmory];
					pro[memmory] = pro[index];
					pro[index] = tmp;
					currentID = pro[index].ID;
				}

			}

		}
		System.out.println("SRT");

		Timesliceprint();
	}

	public void HRN() {
		reset();
		Arrays.fill(time, -1);
		int time1 = 0;
		int index;
		Arrays.sort(pro, (a, b) -> a.arrival - b.arrival);

		index = 0;// 인덱스

		int currentID = pro[index].ID;// 현재작업중인 프로세스 아이디

		while (true) {
			int done = 0;
			for (int i = 0; i < pro.length; i++) {
				if (pro[i].save == 0) {

					done++;
				}

			}
			if (done == pro.length) {

				break;

			}

			for (int i = 0; i < pro.length; i++) {
				if (pro[i].getSave() == 0) {

					continue;
				}
				if (currentID == pro[i].ID) {

					pro[i].running();
					time[time1++] = pro[i].ID;
				} else {
					if (pro[i].count < 2) {
						pro[i].plusWait();

					} else {
						pro[i].justwait();

					}
				}
			}

			if (pro[index].save == 0) {
				if (index < pro.length) {
					if (index + 1 >= pro.length) {
						break;
					}

					int memmory = index + 1;
					process tmp = pro[++index];
					for (int i = index; i < pro.length; i++) {

						if (((double) tmp.waiting + (double) tmp.service)
								/ (double) tmp.waiting > ((double) pro[i].waiting + (double) pro[i].service)
										/ (double) pro[i].waiting
								&& time1 >= pro[i].arrival) {// 대기시간+CPU사용시간/CPU사용시간이 존재하는 작업중에 제일 작은거부터 시작한다.
							memmory = i;
							tmp = pro[i];
						}
					}
					tmp = pro[memmory];
					pro[memmory] = pro[index];
					pro[index] = tmp;
					currentID = pro[index].ID;
				}

			}

		}
		System.out.println("HRN");

		print();
	}
}

public class os {
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		BufferedReader reader = new BufferedReader(new FileReader("src/file.txt"));
		String str;
		int num = 0;
		str = reader.readLine();
		while ((str = reader.readLine()) != null) {
			num++;
		}
		
		process[] pro = new process[num - 1];
		reader = new BufferedReader(new FileReader("src/file.txt"));
		int i = 0;
		str = reader.readLine();
		for (int j = 0; j < num - 1; j++) {
			str = reader.readLine();
			String[] result = str.split(" ");
			String id = result[0];
			pro[i] = new process(id, Integer.parseInt(result[1]), Integer.parseInt(result[2]),
					Integer.parseInt(result[3]), 0, 0, 0);
			i++;
		}
		str = reader.readLine();
		
		int timeslice = Integer.parseInt(str);
		reader.close();
		
		scheduling sche = new scheduling(pro, timeslice);
		sche.FCFS();
		System.out.println("");
		sche.SJF();
		System.out.println("");
		sche.priority1();
		System.out.println("");
		sche.priority2();
		System.out.println("");
		sche.RoundRobin();
		System.out.println("");
		sche.SRT();
		System.out.println("");
		sche.HRN();
	}

}
