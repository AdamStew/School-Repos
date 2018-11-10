import java.awt.*;
import javax.swing.*;
import javax.swing.text.*;
import java.awt.event.*;
import java.util.*;

public class ScrollPaneForText extends JScrollPane
{

    public ScrollPaneForText( JTextArea ta, Dimension minimumSize )
    {
        super( ta );

        setMinimumSize( minimumSize );
        
        // allows auto scrolling within the JTextArea
        DefaultCaret caret = (DefaultCaret)ta.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
    }

}
