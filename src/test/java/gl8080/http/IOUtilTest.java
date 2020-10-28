package gl8080.http;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Test;

public class IOUtilTest {

    @Test
    public void readLine() throws Exception {
	byte[] bytes = { 'a', '\r', '\n' };
	InputStream in = new ByteArrayInputStream(bytes);
	String str = IOUtil.readLine(in);
	assertThat("a", is(str));
    }

    //    @Test
    public void readLine2() throws Exception {
	byte[] bytes = { -1, '\r', '\n' };
	InputStream in = new ByteArrayInputStream(bytes);
	String str = IOUtil.readLine(in);
    }
    
    @Test(expected = EmptyRequestException.class)
    public void tryEmptyRequest() throws Exception {    
	byte[] bytes = { };
	InputStream in = new ByteArrayInputStream(bytes);
	String str = IOUtil.readLine(in);
    }


    /*
     * Another Impl of IOUtil.readLine()
     */
    public static String readLine(InputStream in) throws IOException {
        List<Byte> list = new ArrayList<>();

	int c, prev_c = -1;
        while (true) {
	    c = in.read();
	    if (c == -1) {
		throw new EmptyRequestException();
	    }
	    
	    if (prev_c == '\r' && c == '\n') {
		list.remove(list.size() - 1); // remove tailing '\r'
		break;
	    }
	    
            list.add((byte)c);	    
	    prev_c = c;
        }

	byte[] buffer = new byte[list.size()];
	for (int i = 0; i < list.size(); i++) {
	    buffer[i] = list.get(i);
	}
	
        return new String(buffer);
    }

    //
    // Testing for the another Impl of IOUtil.readLine()
    //
    
    @Test
    public void readLine2_1() throws Exception {
	byte[] bytes = { 'a', '\r', '\n' };
	InputStream in = new ByteArrayInputStream(bytes);
	String str = IOUtilTest.readLine(in);
	assertThat("a", is(str));
    }

    @Test
    public void readLine2_2() throws Exception {
	byte[] bytes = { -1, '\r', '\n' };
	InputStream in = new ByteArrayInputStream(bytes);
	String str = IOUtilTest.readLine(in);
    }
    
    @Test(expected = EmptyRequestException.class)
    public void tryEmptyRequest2() throws Exception {    
	byte[] bytes = { };
	InputStream in = new ByteArrayInputStream(bytes);
	String str = IOUtilTest.readLine(in);
    }
    
}

