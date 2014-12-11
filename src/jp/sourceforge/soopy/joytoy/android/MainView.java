package jp.sourceforge.soopy.joytoy.android;

import java.io.PrintStream;
import java.io.StringReader;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainView extends Activity {
  public static final int ACTIVITY_MAIN=0;
  public static final int ACTIVITY_LIST=1;
  public static final int ACTIVITY_EDIT=2;

  private JoyToy joytoy;

  private Activity me;
  private EditText editor;
  private TextView textView;
  private WebView webView;
  
  //private LinearLayout contentView;
  //private LinearLayout consoleView;
  //private LinearLayout referenceView;

  private Button evalButton;
  private Button loadButton;
  private Button saveButton;
  private Button clearButton;
  private Button referenceButton;

	private Button forwardButton;
	private Button backwardButton;
	private Button returnButton;

  /*
  private static final int INSERT_ID = Menu.FIRST;
  private static final int DELETE_ID = Menu.FIRST + 1;

  private int screenWidth = 0;
  private int screenHeight = 0;
  */
  private final static int FP = ViewGroup.LayoutParams.FILL_PARENT;
  private final static int WC = ViewGroup.LayoutParams.WRAP_CONTENT;
		  

  private NotesDbAdapter mDbHelper;
  private Long mRowId;

  /** Called when the activity is first created. */
  @Override
  public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
    
    this.me = this;
    mDbHelper = new NotesDbAdapter(this);
    mDbHelper.open();

    setContentView(R.layout.main);

     // コンポーネントの設定
    editor = (EditText)this.findViewById(R.id.edit_view);
    textView = (TextView)this.findViewById(R.id.text_view);

    //contentView = (LinearLayout)this.findViewById(R.id.content_view);
    //consoleView = (LinearLayout)this.findViewById(R.id.console_view);
    
    evalButton = (Button)this.findViewById(R.id.eval_button);
    loadButton = (Button)this.findViewById(R.id.load_button);
    saveButton = (Button)this.findViewById(R.id.save_button);
    referenceButton = (Button)this.findViewById(R.id.reference_button);
    clearButton = (Button)this.findViewById(R.id.clear_button);
    

    // イベント組み込み
    evalButton.setOnClickListener(new EvalButtonAdapter());
    loadButton.setOnClickListener(new LoadButtonAdapter());
    saveButton.setOnClickListener(new SaveButtonAdapter());
    referenceButton.setOnClickListener(new RefernceButtonAdapter());
    clearButton.setOnClickListener(new ClearButtonAdapter());

    /*
    // create reference view
    referenceView = (LinearLayout)this.findViewById(R.id.reference_view);
    webView = (WebView)this.findViewById(R.id.reference_web_view);
    forwardButton = (Button)this.findViewById(R.id.forward_button);
    backwardButton = (Button)this.findViewById(R.id.backward_button);
    returnButton = (Button)this.findViewById(R.id.return_button);
    
    forwardButton.setOnClickListener(new OnClickListener(){
  	  public void onClick(View v){
  		  if(v == forwardButton){
  			  webView.goForward();
  		  }
  	  }
    });
    
    backwardButton.setOnClickListener(new OnClickListener(){
  	  public void onClick(View v){
  		  if(v == backwardButton){
  			  webView.goBack();
  		  }
  	  }
    });

    returnButton.setOnClickListener(new OnClickListener(){

      public void onClick(View v) {
        if(v == returnButton){
        	changeContentView(consoleView);
        }
      }

    });
    */

    //changeContentView(consoleView);
    //referenceView.setVisibility(View.VISIBLE);
    //webView.loadUrl("file:///android_asset/index.html");

    String versionName ;
    PackageManager pm = getPackageManager();
    try {
   	 PackageInfo info = null;
   	 info = pm.getPackageInfo("jp.sourceforge.soopy.joytoy.android", 0);
   	  versionName = info.versionName;
    } catch (NameNotFoundException e) {
      versionName = "<unknown>";
    }

    // init JoyToy Language
    if(joytoy == null){
      PrintStream writer = new PrintStream(new JoyToyWriter(textView));
      joytoy = new JoyToy(writer);
      JoyToy.out.println("JoyToy programming language");
      JoyToy.out.println("    version " + versionName);
      JoyToy.out.println();
    }

  }
 
  /*
  private ViewGroup.LayoutParams mkParams(int w, int h) {
	  return new ViewGroup.LayoutParams(w, h);
  }
  */
  
  private LinearLayout.LayoutParams createParam(int w, int h){
      return new LinearLayout.LayoutParams(w, h);
  }

  /*
  private LinearLayout.LayoutParams createParam(int w, int h, float weight){
      return new LinearLayout.LayoutParams(w, h, weight);
  }
  */
  

  private void populateFields() {
    if (mRowId != null) {
        Cursor note = mDbHelper.fetchNote(mRowId);
        startManagingCursor(note);
        this.editor.setText(note.getString(
                note.getColumnIndexOrThrow(NotesDbAdapter.KEY_BODY)));
    }
}


  /*
  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    super.onCreateOptionsMenu(menu);
    menu.add(0, INSERT_ID, 0, R.string.menu_insert);
    menu.add(0, DELETE_ID, 0,  R.string.menu_delete);
    return true;
  }
  */

  /*
  @Override
  public boolean onMenuItemSelected(int featureId, MenuItem item) {
    switch(item.getItemId()) {
    case INSERT_ID:
      createNote();
      return true;
    case DELETE_ID:
      mDbHelper.deleteNote(getListView().getSelectedItemId());
      fillData();
      return true;
    }

    return super.onMenuItemSelected(featureId, item);
  }

  */

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
    super.onActivityResult(requestCode, resultCode, intent);
    switch(requestCode){
    case ACTIVITY_LIST:
    	if(resultCode == RESULT_OK && intent != null){
   	      Bundle extras = intent.getExtras();            
   	      mRowId = extras != null ?
   	          extras.getLong(NotesDbAdapter.KEY_ROWID) 
   	          : null;
          populateFields();
    	}
    	break;
    case ACTIVITY_EDIT:
        if(resultCode == RESULT_OK && intent != null){
          Bundle extras = intent.getExtras();
          String code = extras != null ?
              extras.getString(NotesDbAdapter.KEY_BODY)
              : null;
          if(code != null){
            this.editor.setText(code);
          }
        }
    	break;
    }
  }


  // イベントクラス
  class EvalButtonAdapter implements OnClickListener {
    public void onClick(View v) {
      if(v == evalButton){
        String s = editor.getText().toString();
        StringReader reader = new StringReader(s);

        joytoy.run(reader);
      }
    }
  }

  // イベントクラス
  class LoadButtonAdapter implements OnClickListener {
    public void onClick(View v) {
      if(v == loadButton){
        Intent i = new Intent(me, NotesList.class);
        startActivityForResult(i, ACTIVITY_LIST);
      }
    }
  }

  // イベントクラス
  class SaveButtonAdapter implements OnClickListener {
    public void onClick(View v) {
      if(v == saveButton){
        Intent i = new Intent(me, NoteEdit.class);
        String text = editor.getText().toString();
        i.putExtra(NotesDbAdapter.KEY_BODY, text);
        startActivityForResult(i, ACTIVITY_EDIT);
      }
    }
  }

  // イベントクラス
  class ClearButtonAdapter implements OnClickListener {
    public void onClick(View v) {
      if(v == clearButton){
        textView.setText(null); // clear text view.
      }
    }
  }

  // イベントクラス
  class RefernceButtonAdapter implements OnClickListener {
    Dialog dialog = null;

    public void onClick(View v) {
      if(v == referenceButton){
        if(dialog == null){
          dialog = new Dialog(me);
          dialog.setTitle("JoyToy Reference");

          LinearLayout layout1 = new LinearLayout(me);
          layout1.setOrientation(LinearLayout.VERTICAL);

          if(webView == null){
            webView = new MyWebView(me);
            webView.setLayoutParams(createParam(FP, 160));
          }
          webView.loadUrl("file:///android_asset/index.html");
          layout1.addView(webView);

          forwardButton = new Button(me);
          forwardButton.setText(" > ");

          backwardButton = new Button(me);
          backwardButton.setText(" < ");

          returnButton = new Button(me);
          returnButton.setText(R.string.close);

          LinearLayout layout2 = new LinearLayout(me);
          layout2.setOrientation(LinearLayout.HORIZONTAL);
          layout2.addView(backwardButton);
          layout2.addView(forwardButton);
          layout2.addView(returnButton);

          layout1.addView(layout2);
          dialog.setContentView(layout1);

          resetForwardBackwardButton();
          
          /*
          webView.setOnClickListener(new OnClickListener(){
            public void onClick(View v) {
              resetForwardBackwardButton();
            }
          });
          webView.setOnTouchListener(new OnTouchListener(){

            public boolean onTouch(View v, MotionEvent event) {
              resetForwardBackwardButton();
              return false;
            }
            
          });
          */
          

          forwardButton.setOnClickListener(new OnClickListener(){
        	  public void onClick(View v){
        		  if(v == forwardButton){
        		    webView.goForward();
                    resetForwardBackwardButton();
        		  }
        	  }
          });
          
          backwardButton.setOnClickListener(new OnClickListener(){
        	  public void onClick(View v){
        		  if(v == backwardButton){
        			  webView.goBack();
                      resetForwardBackwardButton();
        		  }
        	  }
          });

          returnButton.setOnClickListener(new OnClickListener(){

            public void onClick(View v) {
              if(v == returnButton){
                dialog.cancel();
              }
            }

          });
        }

        dialog.show();

    	  //consoleView.setVisibility(View.INVISIBLE);
    	  //referenceView.setVisibility(View.VISIBLE);
    	  /*
    	  contentView.removeAllViews();
    	  contentView.addView(referenceView);

    	  
    	  referenceView.invalidate();
    	  */
    	  //changeContentView(referenceView);
    	  

      }
    }

    private void resetForwardBackwardButton() {
      forwardButton.setEnabled(webView.canGoForward());
      backwardButton.setEnabled(webView.canGoBack());
      forwardButton.invalidate();
      backwardButton.invalidate();
    }

    class MyWebView extends WebView {

    	public MyWebView(Context context) {
    		super(context);
    	}

    	/* (non-Javadoc)
    	 * @see android.webkit.WebView#onDraw(android.graphics.Canvas)
    	 */
    	@Override
    	protected void onDraw(Canvas canvas) {
    		super.onDraw(canvas);
    		resetForwardBackwardButton();
    	}

      }
    }
  
  /*
  private void changeContentView(View v){
	  contentView.removeAllViews();
	  contentView.addView(v);
	  if(v == referenceView){
	    webView.bringToFront();
	  }
	  v.invalidate();
  }
  */

  /*
  // イベントクラス
  class ReturnButtonAdapter implements OnClickListener {
    public void onClick(View v) {
      if(v == returnButton){
    	  consoleView.setVisibility(View.VISIBLE);
    	  referenceView.setVisibility(View.INVISIBLE);
    	  contentView.removeAllViews();
    	  contentView.addView(consoleView);
    	  consoleView.invalidate();
      }
    }
  }
  */
  


}
