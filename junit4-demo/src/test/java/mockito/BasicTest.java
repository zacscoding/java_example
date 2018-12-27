package mockito;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.RETURNS_SMART_NULLS;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.stream.IntStream;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;

/**
 * @author zacconding
 * @Date 2018-11-28
 * @GitHub : https://github.com/zacscoding
 */
public class BasicTest {

    List mockedList;

    @Before
    public void setUp() {
        mockedList = mock(List.class);
    }

    @Test
    public void usage_verify() {
        mockedList.add("item");

        verify(mockedList).add("item");
        verify(mockedList, times(1)).add("item");

        mockedList.clear();
        verify(mockedList).clear();

        verify(mockedList, never()).add("box");
        verify(mockedList, atLeastOnce()).clear();
        mockedList.size();
        mockedList.size();
        verify(mockedList, atLeast(2)).size();
    }

    @Test
    public void usage_verifyWithArgsMatcher() {
        IntStream.range(0, 5).forEach(i -> mockedList.add("hivava" + i));
        verify(mockedList, times(5)).add(any());
    }

    @Test
    public void usage_inOrder() {
        List firstMock = mock(List.class);
        List secondMock = mock(List.class);

        firstMock.add("hiva1");
        secondMock.add("hiva2");

        InOrder inOrder = inOrder(firstMock, secondMock);
        inOrder.verify(firstMock).add("hiva1");
        inOrder.verify(secondMock).add("hiva2");
        // fail if
        // secondMock.add("hiva2");
        // firstMock.add("hiva1");
    }

    @Test
    public void usage_when() {
        // stub 만들기
        when(mockedList.get(0)).thenReturn("item");
        when(mockedList.size()).thenReturn(1);
        when(mockedList.get(1)).thenThrow(new RuntimeException());

        assertThat(mockedList.get(0), is("item"));
        assertTrue(mockedList.size() == 1);
        try {
            mockedList.get(1);
            fail();
        } catch (RuntimeException e) {
        }
    }

    @Test
    public void usage_whenWithVoidReturnToStub() {
        doThrow(new RuntimeException()).when(mockedList).clear();
        try {
            mockedList.clear();
            fail();
        } catch (RuntimeException e) {
        }
    }

    @Test
    public void usage_smartNulls() {
        List firstMock = mock(List.class);
        List secondMock = mock(List.class, RETURNS_SMART_NULLS);

        System.out.println(firstMock.toArray());
        System.out.println(secondMock.toArray());
    }
}
