#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.boot.config;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.Locale;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MessageManagerTest {

    @Mock
    private MessageSource messageSource;
    @InjectMocks
    private MessageManager messageManager;
    @Before
    public void before(){
        Locale locale = LocaleContextHolder.getLocale();
        when(messageSource.getMessage("CODE_TEST", new Object[0],locale)).thenReturn("TEST-MESSAGE");
    }
    @Test
    public void test(){
        String message = messageManager.message("CODE_TEST");
        assertEquals(message,"TEST-MESSAGE");
    }
    @Test
    public void testNull(){
        MessageManager messageManager = new MessageManager(null);
        String message = messageManager.message("CODE_TEST");
        assertNull(message);
    }
}