package XMLreporter;

public class Main {
	public static void main(String[] args) {
		
		Tree<String> t =new Tree<String>("top",
				new Tree[] {
					new Tree("sub1"),
					new Tree("sub2")
				});
		/*
		Tree<String> t =new Tree<String>("top",
				new Tree[] {
					new Tree("sub1"),
					new Tree("sub2" , 
						new Tree[] {
							new Tree("subsub2")
						}),
					new Tree("sub3")
					});
		*/
		
	    Saver s = new Saver();
	    try {
	        String r = s.save(t);
	        System.out.println(r);
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
}
