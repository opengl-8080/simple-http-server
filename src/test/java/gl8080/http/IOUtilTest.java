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

    /**
     * readline() normal testing
     */ 
    @Test
    public void readLineN() throws Exception {
	byte[] bytes = "GET / HTTP/1.1\r\n".getBytes();
	InputStream in = new ByteArrayInputStream(bytes);
	String str = IOUtil.readLine(in);
	assertThat("GET / HTTP/1.1", is(str));
    }

    /*
     * readLine() reads data includes isolated CR and LF.
     */
    @Test
    public void isolatedCRandLF() throws Exception {    
	byte[] bytes = "include isolated \r and \n\r\n".getBytes();
	InputStream in = new ByteArrayInputStream(bytes);
	String str = IOUtil.readLine(in);
	assertThat("include isolated \r and \n", is(str));
    }

    /**
     * readline() boundary testing 0
     */ 
    @Test
    public void readLineB0() throws Exception {
	byte[] bytes = "\r\n".getBytes();
	InputStream in = new ByteArrayInputStream(bytes);
	String str = IOUtil.readLine(in);
	assertThat("", is(str));
    }

    /**
     * readline() boundary testing 1
     */ 
    @Test
    public void readLineB1() throws Exception {
	byte[] bytes = "A\r\n".getBytes();
	InputStream in = new ByteArrayInputStream(bytes);
	String str = IOUtil.readLine(in);
	assertThat("A", is(str));
    }
    
    /**
     * readLine() can reads 0xFF(-1) as not EOF but data.
     * without throwing EmptyRequestException. 
     */
    @Test
    public void readLine2() throws Exception {
	byte[] bytes = { (byte)0xFF, '\r', '\n' };
	InputStream in = new ByteArrayInputStream(bytes);
	String str = IOUtil.readLine(in);
    }
    
    /*
     * readLine() reads empty byte stream then throw
     * EmptyRequestException.
     */
    @Test(expected = EmptyRequestException.class)
    public void throwEmptyRequest() throws Exception {    
	byte[] bytes = { }; 
	InputStream in = new ByteArrayInputStream(bytes);
	String str = IOUtil.readLine(in);
    }
}

