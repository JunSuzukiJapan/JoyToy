package jp.sourceforge.soopy.joytoy.android;

import java.io.IOException;
import java.io.OutputStream;

import android.widget.TextView;

public class JoyToyWriter extends OutputStream {
	private TextView textView;

	public JoyToyWriter(TextView view){
		super();

		this.textView = view;
	}

	@Override
	public void write(int oneByte) throws IOException {
		StringBuilder text = new StringBuilder();
		text.append((char)oneByte);
		textView.append(text);
	}

  @Override
  public void flush() throws IOException {
    super.flush();
    textView.invalidate();
  }

  @Override
  public void write(byte[] buffer, int offset, int count) throws IOException {
    //super.write(buffer, offset, count);
    StringBuilder builder = new StringBuilder();
    for(int i = 0; i < count; ++i){
      builder.append((char)buffer[i + offset]);
    }
    textView.append(builder);
  }

}