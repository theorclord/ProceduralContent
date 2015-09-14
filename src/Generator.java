import java.util.ArrayList;
import java.util.Random;

public class Generator {

	private static int[] size = new int[]{20,40};
	private static int maxNumOfRooms = 10;
	private static int roomAvgSize = 3;
	
	public static void main(String[] args) {
		Random ran = new Random();
		String[][] dungeon = new String[size[0]][size[1]];
		for(int i = 0; i<dungeon.length;i++){
			for(int j = 0; j<dungeon[i].length;j++){
				dungeon[i][j] = " ";
			}
		}
		ArrayList<Room> arrList = new ArrayList<Room>();
		/*
		Room r1 = new Room(new int[]{18,1},new int[]{1,1});
		addRoom(r1,dungeon);
		arrList.add(r1);
		Room r2 = new Room(new int[]{18,38},new int[]{1,1});
		addRoom(r2,dungeon);
		arrList.add(r2);
		Room r3 = new Room(new int[]{1,1},new int[]{1,1});
		addRoom(r3,dungeon);
		arrList.add(r3);
		Room r4 = new Room(new int[]{1,38},new int[]{1,1});
		addRoom(r4,dungeon);
		arrList.add(r4);
		Room r5 = new Room(new int[]{12,25},new int[]{2,3});
		addRoom(r5,dungeon);
		arrList.add(r5);
		*/
		//Add rooms
		for(int i = 0; i<maxNumOfRooms; i++){
			int[] roomCenter = new int[]{ran.nextInt(size[0]), ran.nextInt(size[1])};
			int[] roomSize = new int[]{ran.nextInt(roomAvgSize)+1,ran.nextInt(roomAvgSize)+1};
			Room r = new Room(roomCenter, roomSize);
			
			boolean roomAdded = addRoom(r, dungeon);
			if(roomAdded){
				arrList.add(r);
			}
		}
		//connect rooms
		for(int i = 0; i<arrList.size();i++){
			Room tempRoom = arrList.get(i);
			if(tempRoom.connected){
				continue;
			}
			int minVal = -1;
			Room connectRoom = null;
			for(int j = 0; j<arrList.size();j++){
				if(i==j){
					continue;
				}
				Room connectRoomTemp = arrList.get(j);
				int temp = Math.abs(tempRoom.center[0]-connectRoomTemp.center[0]) + Math.abs(tempRoom.center[1] - connectRoomTemp.center[1]);
				if((temp < minVal || minVal == -1 )&& !connectRoomTemp.connected ){
					minVal = temp;
					connectRoom = connectRoomTemp;
				}
			}
			if(connectRoom != null){
				connectRooms(tempRoom, connectRoom, dungeon);
			}
			tempRoom.connected = true;
		}
		//add exit and entrance.
		int exit = ran.nextInt(arrList.size());
		int entrance = exit;
		while(entrance == exit){
			entrance = ran.nextInt(arrList.size());
		}
		
		boolean[][] passMap = new boolean[20][40];
		for(int i = 0; i<passMap.length;i++){
			for(int j=0; j<passMap[i].length;j++){
				if(dungeon[i][j] == "R" || dungeon[i][j] == "D"
						|| dungeon[i][j] == "T"){
					passMap[i][j] = true;
				}
			}
		}
		int exitDist = AStar.distance(passMap, arrList.get(entrance).center[0], arrList.get(entrance).center[1], arrList.get(exit).center[0], arrList.get(exit).center[1]);
		
		//check fitness
		
		
		printDungeon(dungeon);
	}
	
	private static void printDungeon(String[][] dungeon) {
		//print dungeon
		for(int i = 0; i<size[1]+2;i++){
			System.out.print("#");
		}
		System.out.println();
		for(int i = 0; i<dungeon.length;i++){
			System.out.print("#");
			for(int j = 0; j<dungeon[i].length;j++){
				System.out.print(dungeon[i][j]);
			}
			System.out.println("#");
		}
		for(int i = 0; i<size[1]+2;i++){
			System.out.print("#");
		}
	}

	private static void connectRooms(Room r1, Room r2, String[][] dungeon) {
		//x
		int xval = -1;
		System.out.println("x center dist " + (r1.center[0]-r2.center[0]));
		for(int i =0; i<Math.abs(r1.center[0]-r2.center[0]);i++){
			xval = (int) (r2.center[0]+Math.signum(r1.center[0]-r2.center[0])*i);
			//dungeon[r2.center[0]+i][r1.center[1]] = "T";
			switch (dungeon[xval][r1.center[1]]) {
			case " ":
				dungeon[xval][r1.center[1]] = "T";
				break;
			case "W":
				dungeon[xval][r1.center[1]] = "D";
			case "R":
				continue;
			case "T":
				continue;
			default:
				break;
			}
		}
		//y
		System.out.println("y center dist " + (r1.center[1]-r2.center[1]));
		for(int i =0; i<Math.abs(r1.center[1]-r2.center[1]);i++){
			int yval = (int) (r2.center[1]+Math.signum(r1.center[1]-r2.center[1])*i);
			if(xval == -1){
				xval = r2.center[0];
			}
			//System.out.println(r2.center[1]+i);
			//printDungeon(dungeon);
			
			switch (dungeon[r2.center[0]][yval]) {
			case " ":
				dungeon[r2.center[0]][yval] = "T";
				break;
			case "W":
				dungeon[r2.center[0]][yval] = "D";
			case "R":
				continue;
			case "T":
				continue;
			default:
				break;
			}
			
		}
		
	}

	private static boolean addRoom(Room r, String[][] dungeon){
		for(int x = -r.size[0];x<=r.size[0];x++){
			for(int y = -r.size[1];y<=r.size[1];y++){
				//check x
				if(r.center[0]+x >= dungeon.length || r.center[0]+x <0){
					return false;
				}
				//check y
				if(r.center[1]+y >= dungeon[0].length || r.center[1]+y <0){
					return false;
				}
				//check availability
				if(dungeon[r.center[0]+x][r.center[1]+y] != " "){
					return false;
				}
			}
		}
		
		for(int x = - r.size[0]; x<=r.size[0];x++){
			for(int y = - r.size[1]; y<=r.size[1];y++){
				if(y== -r.size[1] || x== -r.size[0] || y== r.size[1] || x== r.size[0]){
					dungeon[r.center[0]+x][r.center[1]+y] = "W";
				} else{
				dungeon[r.center[0]+x][r.center[1]+y] = "R";
				}
			}
		}
		return true;
	}

}
