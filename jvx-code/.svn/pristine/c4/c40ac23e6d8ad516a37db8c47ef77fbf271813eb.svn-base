package research;

import javax.swing.JFrame;
import javax.swing.JTextPane;
import javax.swing.text.AbstractDocument;
import javax.swing.text.BoxView;
import javax.swing.text.ComponentView;
import javax.swing.text.Element;
import javax.swing.text.IconView;
import javax.swing.text.LabelView;
import javax.swing.text.ParagraphView;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import javax.swing.text.StyledEditorKit;
import javax.swing.text.View;
import javax.swing.text.ViewFactory;

public class TestTextPane {
JTextPane edit=new JTextPane();
public TestTextPane() {
JFrame frame=new JFrame();
frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
frame.getContentPane().add(edit);
try {
edit.setEditorKit(new MyEditorKit());
SimpleAttributeSet attrs=new SimpleAttributeSet();
StyleConstants.setAlignment(attrs,StyleConstants.ALIGN_CENTER);
StyledDocument doc=(StyledDocument)edit.getDocument();
doc.insertString(0,"111\n2222222\n33333333333333",attrs);
doc.setParagraphAttributes(0,doc.getLength()-1,attrs,false);
}
catch (Exception ex) {
ex.printStackTrace();
}

frame.setSize(300,300);
frame.setLocationRelativeTo(null);
frame.setVisible(true);
}
public static void main(String[] args) throws Exception {
new TestTextPane();
}
}

class MyEditorKit extends StyledEditorKit {
public ViewFactory getViewFactory() {
return new StyledViewFactory();
}

static class StyledViewFactory implements ViewFactory {
public View create(Element elem) {
String kind = elem.getName();
if (kind != null) {
if (kind.equals(AbstractDocument.ContentElementName)) {
return new LabelView(elem);
} else if (kind.equals(AbstractDocument.ParagraphElementName)) {
return new ParagraphView(elem);
} else if (kind.equals(AbstractDocument.SectionElementName)) {
return new CenteredBoxView(elem, View.Y_AXIS);
} else if (kind.equals(StyleConstants.ComponentElementName)) {
return new ComponentView(elem);
} else if (kind.equals(StyleConstants.IconElementName)) {
return new IconView(elem);
}
}

// default to text display
return new LabelView(elem);
}
}
}

class CenteredBoxView extends BoxView {
public CenteredBoxView(Element elem, int axis) {
super(elem,axis);
}
protected void layoutMajorAxis(int targetSpan, int axis, int[] offsets, int[] spans) {
super.layoutMajorAxis(targetSpan,axis,offsets,spans);
int textBlockHeight = 0;
int offset = 0;

for (int i = 0; i < spans.length; i++) {
textBlockHeight += spans[ i ];
}
offset = (targetSpan - textBlockHeight) / 2;
for (int i = 0; i < offsets.length; i++) {
offsets[ i ] += offset;
}
}
} 
