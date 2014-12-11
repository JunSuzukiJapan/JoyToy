package jp.sourceforge.soopy.joytoy.android;

import java.io.File;
import java.util.ArrayList;

public class JTPrimAndroid {

	public class JTPrimPWD extends JTPrim {

		public JTPrimPWD() {
			super(0);
		}

		@Override
		protected JTCode exec(ArrayList<JTCode> params) throws Exception {
			File f = new File("");
			String path = f.getAbsolutePath();
			JoyToy.out.println(path);

			return null;
		}

	}

	public class JTPrimLS extends JTPrim {

		public JTPrimLS() {
			super(0);
		}

		@Override
		protected JTCode exec(ArrayList<JTCode> params) throws Exception {
			File f = new File("/");
			String[] files = f.list();
			if(files != null){
				for(int i = 0; i < files.length; ++i){
					JoyToy.out.println(files[i]);
				}
			}
			
			return null;
		}
		
	}


}
