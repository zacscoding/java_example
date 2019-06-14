package mockito;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.AbstractList;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

/**
 * https://www.baeldung.com/mockito-void-methods
 *
 * @author zacconding
 */
public class MockitoTutorial {

    @Test
    public void whenAddCalledVerified() {
        MyList myList = mock(MyList.class);

        doNothing().when(myList).add(isA(Integer.class), isA(String.class));
        myList.add(0, "");

        verify(myList, times(1)).add(0, "");
    }

    @Test(expected = Exception.class)
    public void givenNullAddAndThenThrows() {
        MyList myList = mock(MyList.class);
        doThrow().when(myList).add(isA(Integer.class), isNull());

        myList.add(0, null);
    }

    @Test
    public void whenAddCalledValueCaptured() {
        MyList myList = mock(MyList.class);
        ArgumentCaptor<String> valueCapture = ArgumentCaptor.forClass(String.class);
        doNothing().when(myList).add(any(Integer.class), valueCapture.capture());

        myList.add(0, "hivava");
        assertThat(valueCapture.getValue(), is("hivava"));
    }

    @Test
    public void whenAddCalledAnswered() {
        MyList myList = mock(MyList.class);
        doAnswer(invocation -> {
            Object arg0 = invocation.getArgument(0);
            Object arg1 = invocation.getArgument(1);

            assertEquals(3, arg0);
            assertEquals("answer me", arg1);
            return null;
        }).when(myList).add(any(Integer.class), any(String.class));

        myList.add(3, "answer me");
    }

    @Test
    public void whenAddCalledRealMethodCalled() {
        MyList myList = mock(MyList.class);
        doCallRealMethod().when(myList).add(anyInt(), anyString());
        myList.add(1, "real");
        verify(myList, times(1)).add(1, "real");
    }


    private static class MyList extends AbstractList<String> {

        @Override
        public void add(int index, String element) {
            System.out.println("called add");
            // no-op
        }

        @Override
        public String get(int index) {
            return null;
        }

        @Override
        public int size() {
            return 0;
        }
    }
}
