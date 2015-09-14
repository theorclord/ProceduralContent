import java.util.ArrayList;
import java.util.Random;


public class Generator {

	public static void main(String[] args) {
		
		Random ran = new Random();
		int numOfRooms = 10;
		int roomAvgSize = 2;
		int[] size = new int[]{20,40};
		
		String[][] dungeon = new String[size[0]][size[1]];
		for(int i = 0; i<dungeon.length;i++){
			for(int j = 0; j<dungeon[i].length;j++){
				dungeon[i][j] = " ";
			}
		}
		
		ArrayList<Room> arrList = new ArrayList<Room>();
		//addRoom(new Room(new int[]{18,18},new int[]{1,1}),dungeon);
		//addRoom(new Room(new int[]{18,38},new int[]{1,1}),dungeon);
		
		//Add rooms
		for(int i = 0; i<numOfRooms; i++){
			int[] roomCenter = new int[]{ran.nextInt(size[0]), ran.nextInt(size[1])};
			int[] roomSize = new int[]{ran.nextInt(roomAvgSize)+1,ran.nextInt(roomAvgSize)+1};
			
			arrList.add(new Room(roomCenter, roomSize));
			addRoom(new Room(roomCenter, roomSize), dungeon);
		}
		//connect rooms
		//Create map
		boolean[][] passMap = new boolean[size[0]][size[1]];
		for(int x=0; x<passMap.length;x++){
			for(int y=0; y<passMap[x].length;y++){
				if(dungeon[x][y] == " "){
					passMap[x][y] = true;
				}
			}
		}
		for(int i = 0; i<arrList.size();i++){
			if(arrList.get(i).connected){
				continue;
			}
			int minVal = -1;
			for(int j = 0; j<arrList.size();j++){
				int temp = AStar.distance(passMap, arrList.get(i).center[0], arrList.get(i).center[1], arrList.get(j).center[0], arrList.get(j).center[1]);
				if(temp < minVal || minVal == -1){
					minVal = temp;
				}
			}
		}
		
		
		
		
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
	
	private static void addRoom(Room r, String[][] dungeon){
		for(int x = -r.size[0];x<=r.size[0];x++){
			for(int y = -r.size[1];y<=r.size[1];y++){
				//check x
				if(r.center[0]+x >= dungeon.length || r.center[0]+x <0){
					return;
				}
				//check y
				if(r.center[1]+y >= dungeon[0].length || r.center[1]+y <0){
					return;
				}
				//check availability
				if(dungeon[r.center[0]+x][r.center[1]+y] != " "){
					return;
				}
			}
		}
		
		for(int x = - r.size[0]; x<=r.size[0];x++){
			for(int y = - r.size[1]; y<=r.size[1];y++){
				if(y== -r.size[1] || x== -r.size[0] || y== r.size[1] || x== r.size[0]){
					dungeon[r.center[0]+x][r.center[1]+y] = "w";
				} else{
				dungeon[r.center[0]+x][r.center[1]+y] = "r";
				}
			}
		}
	}

}
