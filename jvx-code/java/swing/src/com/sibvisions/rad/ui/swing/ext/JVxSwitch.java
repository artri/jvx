/*
 * Copyright 2014 SIB Visions GmbH
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 *
 *
 * History
 *
 * 28.03.2014 - [HM] - creation
 */
package com.sibvisions.rad.ui.swing.ext;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.KeyboardFocusManager;
import java.awt.RenderingHints;
import java.awt.Window;
import java.awt.geom.RoundRectangle2D;
import java.util.function.Predicate;

import javax.swing.AbstractButton;
import javax.swing.Action;
import javax.swing.ButtonModel;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import com.sibvisions.rad.ui.swing.impl.SwingFactory;

/**
 * The <code>JVxCheckBox</code> class is a check box that checks the mouse position.
 * The state changes only, if the mouse position is inside the check box rectangle.
 *  
 * @author Martin Handsteiner
 */
public class JVxSwitch extends JVxCheckBox
{
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Class members
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    /** The default switch icon. */
    private Icon defaultIcon;
    
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Creates an initially unselected check box with no set text or icon.
     */
	public JVxSwitch()
	{
		super();
	}
	
    /**
     * Creates a check box whose properties are taken from the Action supplied.
     *
     * @param pAction the action of the check box
     */
	public JVxSwitch(Action pAction)
	{
		super(pAction);
	}
	
    /**
     * Creates an initially unselected check box with an icon.
     *
     * @param pIcon the icon of the check box
     */
	public JVxSwitch(Icon pIcon)
	{
		super(pIcon);
	}
	
    /**
     * Creates an initially unselected check box with text.
     *
     * @param pText the text of the check box
     */
	public JVxSwitch(String pText)
	{
		super(pText);
	}
	
    /**
     * Creates a check box with the specified text and selection state.
     *
     * @param pText the text of the check box
     * @param pSelected the selected state of the check box
     */
	public JVxSwitch(String pText, boolean pSelected)
	{
		super(pText, pSelected);
	}
	
    /**
     * Creates an initially unselected check box with the specified text and icon.
     *
     * @param pText the text of the check box
     * @param pIcon the icon of the check box
     */
	public JVxSwitch(String pText, Icon pIcon)
	{
		super(pText, pIcon);
	}
	
    /**
     * Creates a check box with the specified text, icon, and selection state.
     *
     * @param pText the text of the check box
     * @param pIcon the icon of the check box
     * @param pSelected the selected state of the check box
     */
	public JVxSwitch(String pText, Icon pIcon, boolean pSelected)
	{
		super(pText, pIcon, pSelected);
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Overwritten methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
    /**
     * {@inheritDoc}
     */
    @Override
    public void updateUI() 
    {
        super.updateUI(); 
        
        defaultIcon = null;
    }
    
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Icon getIcon()
	{
	    Icon icon = super.getIcon();
	    
	    if (icon == null)
	    {
	        return getDefaultIcon();
	    }
	    else
	    {
	        return icon;
	    }
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * The default switch icon for rendering the switch.
	 * 
	 * @return default switch icon for rendering the switch.
	 */
	public Icon getDefaultIcon()
	{
	    if (defaultIcon == null)
	    {
	        defaultIcon = new SwitchIcon();
	    }
	    
	    return defaultIcon;
	}
	
	/**
	 * <code>SwitchIcon</code> renders the switch for max, windows or flatlaf.
	 * 
	 * @author mhandsteiner
	 */
	public static class SwitchIcon implements Icon
	{
	    /** The focus width. */
	    protected float focusWidth =  getUIFloat("CheckBox.icon.focusWidth",  getUIFloat("Component.focusWidth", getDefaultFocusWidth()));
        /** The border width. */
	    protected float borderWidth = getUIFloat("CheckBox.icon.borderWidth", getUIFloat("Component.borderWidth", getDefaultBorderWidth()));
        /** The icon width. */
        protected float iconWidth = getUIFloat("RadioButton.icon.width", 30);
        /** The icon height. */
        protected float iconHeight = getUIFloat("RadioButton.icon.height", 15);
        /** The icon height. */
        protected float arcDiameter = getUIFloat("RadioButton.icon.arcDiameter", 15);
        /** The center diameter. */
	    protected float centerDiameter = getUIFloat("RadioButton.icon.centerDiameter", 8);
        /** The focus color. */
	    protected Color focusColor = getUIColor("CheckBox.icon.focusColor", getUIColor("Component.focusColor", getDefaultFocusColor()));
        /** The border color. */
	    protected Color borderColor = getUIColor("CheckBox.icon.borderColor", getDefaultBorderColor());
        /** The selected border color. */
	    protected Color selectedBorderColor = getUIColor("CheckBox.icon.selectedBorderColor", getDefaultSelectedBorderColor());
        /** The disabled border color. */
	    protected Color disabledBorderColor = getUIColor("CheckBox.icon.disabledBorderColor", null);
        /** The disabled selected border color. */
	    protected Color disabledSelectedBorderColor = getUIColor("CheckBox.icon.disabledSelectedBorderColor", null);
        /** The focused border color. */
	    protected Color focusedBorderColor = getUIColor("CheckBox.icon.focusedBorderColor", null);
        /** The focused selected border color. */
	    protected Color focusedSelectedBorderColor = getUIColor("CheckBox.icon.focusedSelectedBorderColor", null);
        /** The hover border color. */
	    protected Color hoverBorderColor = getUIColor("CheckBox.icon.hoverBorderColor", null);
        /** The hover selected border color. */
	    protected Color hoverSelectedBorderColor = getUIColor("CheckBox.icon.hoverSelectedBorderColor", null);
        /** The pressed border color. */
	    protected Color pressedBorderColor = getUIColor("CheckBox.icon.pressedBorderColor", null);
        /** The pressed selected border color. */
	    protected Color pressedSelectedBorderColor = getUIColor("CheckBox.icon.pressedSelectedBorderColor", null);
        /** The background. */
	    protected Color background = getUIColor("CheckBox.icon.background", getDefaultBackground());
        /** The selected background. */
	    protected Color selectedBackground = getUIColor("CheckBox.icon.selectedBackground", getDefaultSelectedBackground());
        /** The disabled background. */
	    protected Color disabledBackground = getUIColor("CheckBox.icon.disabledBackground", getDefaultDisabledBackground());
        /** The disabled selected background. */
	    protected Color disabledSelectedBackground = getUIColor("CheckBox.icon.disabledSelectedBackground", getDefaultDisabledSelectedBackground());
        /** The focused background. */
	    protected Color focusedBackground = getUIColor("CheckBox.icon.focusedBackground", null);
        /** The focused selected background. */
	    protected Color focusedSelectedBackground = getUIColor("CheckBox.icon.focusedSelectedBackground", null);
        /** The hover background. */
	    protected Color hoverBackground = getUIColor("CheckBox.icon.hoverBackground", null);
        /** The hover selected background. */
	    protected Color hoverSelectedBackground = getUIColor("CheckBox.icon.hoverSelectedBackground", null);
        /** The pressed background. */
	    protected Color pressedBackground = getUIColor("CheckBox.icon.pressedBackground", null);
        /** The pressed selected background. */
	    protected Color pressedSelectedBackground = getUIColor("CheckBox.icon.pressedSelectedBackground", null);
        /** The check mark color. */
	    protected Color checkmarkColor = getUIColor("CheckBox.icon.checkmarkColor", getDefaultCheckmarkColor());
        /** The disabled check mark color. */
	    protected Color disabledCheckmarkColor = getUIColor("CheckBox.icon.disabledCheckmarkColor", getDefaultDisabledCheckmarkColor());
        /** The focused check mark color. */
	    protected Color focusedCheckmarkColor = getUIColor("CheckBox.icon.focusedCheckmarkColor", null);
        /** The hover check mark color. */
	    protected Color hoverCheckmarkColor = getUIColor("CheckBox.icon.hoverCheckmarkColor", null);
        /** The pressed check mark color. */
	    protected Color pressedCheckmarkColor = getUIColor("CheckBox.icon.pressedCheckmarkColor", null);
	    
        /**
         * Constructs a new <code>SwitchIcon</code>.
         */
	    public SwitchIcon()
	    {
	    }
	    
	    /**
	     * {@inheritDoc}
	     */
        @Override
        public void paintIcon(Component pComp, Graphics pG, int pX, int pY)
        {
            Graphics2D g2 = (Graphics2D)pG;
            Object oldKeyAntiAliasing = g2.getRenderingHint(RenderingHints.KEY_ANTIALIASING);
            Object oldKeyStrokeControl = g2.getRenderingHint(RenderingHints.KEY_STROKE_CONTROL);

            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_NORMALIZE);
            
            AbstractButton button = pComp instanceof AbstractButton ? (AbstractButton)pComp : null;
            ButtonModel buttonModel = button == null ? null : button.getModel();
            boolean focused = isPermanentFocusOwner(pComp) && (button == null || button.isFocusPainted());
            boolean selected = buttonModel != null && buttonModel.isSelected(); 
                    
            if (focused && focusWidth > 0)
            {
                g2.setColor(getFocusBorderColor(pComp, buttonModel, selected, focused));
                paintFocusBorder(pComp, g2, pX, pY);
            }
            
            g2.setColor(getBorderColor(pComp, buttonModel, selected, focused));
            paintBorder(pComp, g2, pX, pY);
            
            g2.setColor(JVxUtil.getAverageColor(getBackground(pComp, buttonModel, selected, focused), 
                    selected && selectedBackground != null ? selectedBackground : background));
            paintBackground(pComp, g2, pX, pY);
            
            g2.setColor(getCheckmarkColor(pComp, buttonModel, selected, focused));
            paintCheckmark(pComp, g2, pX, pY, selected);
            
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, oldKeyAntiAliasing);
            g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, oldKeyStrokeControl);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public int getIconWidth()
        {
            return Math.round(iconWidth);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public int getIconHeight()
        {
            return Math.round(iconHeight);
        }
	    
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        // User-defined methods
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        /**
         * Paints the focus border.
         * @param pComp the component
         * @param pG the graphics
         * @param pX the x
         * @param pY the y
         */
        protected void paintFocusBorder(Component pComp, Graphics2D pG, int pX, int pY)
        {
            float fWidth = iconWidth + 2 * focusWidth;
            float fHeight = iconHeight + 2 * focusWidth;
            float fArcDiameter = arcDiameter + 2 * focusWidth;
            
            pG.fill(new RoundRectangle2D.Float(pX - focusWidth, pY - focusWidth, fWidth, fHeight, fArcDiameter, fArcDiameter));
        }

        /**
         * Paints the border.
         * @param pComp the component
         * @param pG the graphics
         * @param pX the x
         * @param pY the y
         */
        protected void paintBorder(Component pComp, Graphics2D pG, int pX, int pY)
        {
            pG.fill(new RoundRectangle2D.Float(pX, pY, iconWidth, iconHeight, arcDiameter, arcDiameter));
        }

        /**
         * Paints the background.
         * @param pComp the component
         * @param pG the graphics
         * @param pX the x
         * @param pY the y
         */
        protected void paintBackground(Component pComp, Graphics2D pG, int pX, int pY)
        {
            float bWidth = iconWidth - 2 * borderWidth;
            float bHeight = iconHeight - 2 * borderWidth;
            float bArcDiameter = Math.max(0, arcDiameter - 2 * borderWidth);
            pG.fill(new RoundRectangle2D.Float(pX + borderWidth, pY + borderWidth, bWidth, bHeight, bArcDiameter, bArcDiameter));
        }

        /**
         * Paints the check mark.
         * @param pComp the component
         * @param pG the graphics
         * @param pX the x
         * @param pY the y
         * @param pSelected the selected state.
         */
        protected void paintCheckmark(Component pComp, Graphics2D pG, int pX, int pY, boolean pSelected)
        {
            float xy = (iconHeight - centerDiameter) / 2f;
            float bArcDiameter = Math.max(0, arcDiameter - 2 * borderWidth);
            pG.fill(new RoundRectangle2D.Float(pX + (pSelected ? iconWidth - iconHeight + xy : xy), pY + xy, centerDiameter, centerDiameter, bArcDiameter, bArcDiameter));
        }
        
        /**
         * Gets the default focus width, if not flatlaf.
         * @return the default focus width, if not flatlaf.
         */
        protected float getDefaultFocusWidth()
        {
            return SwingFactory.isMacLaF() ? 2 : 0;
        }
        
        /**
         * Gets the default border width, if not FlatLaf.
         * @return the default border width, if not FlatLaf.
         */
        protected float getDefaultBorderWidth()
        {
            return 1;
        }
        
        /**
         * Gets the default focus width, if not FlatLaf.
         * @return the default focus width, if not FlatLaf.
         */
        protected Color getDefaultFocusColor()
        {
            return SwingFactory.isMacLaF() ? new Color(142, 158, 175) : null;
        }
        
        /**
         * Gets the default border color, if not FlatLaf.
         * @return the default border color, if not FlatLaf.
         */
        protected Color getDefaultBorderColor()
        {
            return SwingFactory.isMacLaF() ? new Color(188, 188, 188) : new Color(98, 98, 98);
        }

        /**
         * Gets the default selected border color, if not FlatLaf.
         * @return the default selected border color, if not FlatLaf.
         */
        protected Color getDefaultSelectedBorderColor()
        {
            return SwingFactory.isMacLaF() ? new Color(16, 131, 255) : new Color(0, 95, 184);
        }

        /**
         * Gets the default background, if not FlatLaf.
         * @return the default background, if not FlatLaf.
         */
        protected Color getDefaultBackground()
        {
            return SwingFactory.isMacLaF() ? new Color(255, 255, 255) : new Color(243, 243, 243);
        }

        /**
         * Gets the default selected background, if not FlatLaf.
         * @return the default selected background, if not FlatLaf.
         */
        protected Color getDefaultSelectedBackground()
        {
            return SwingFactory.isMacLaF() ? new Color(16, 131, 255) : new Color(0, 95, 184);
        }

        /**
         * Gets the default disabled background, if not FlatLaf.
         * @return the default disabled background, if not FlatLaf.
         */
        protected Color getDefaultDisabledBackground()
        {
            return new Color(249, 249, 249);
        }

        /**
         * Gets the default disabled selected background, if not FlatLaf.
         * @return the default disabled selected background, if not FlatLaf.
         */
        protected Color getDefaultDisabledSelectedBackground()
        {
            return new Color(195, 195, 195);
        }
        
        /**
         * Gets the default check mark color, if not FlatLaf.
         * @return the default check mark color, if not FlatLaf.
         */
        protected Color getDefaultCheckmarkColor()
        {
            return new Color(255, 255, 255);
        }

        /**
         * Gets the default disabled check mark color, if not FlatLaf.
         * @return the default disabled mark color, if not FlatLaf.
         */
        protected Color getDefaultDisabledCheckmarkColor()
        {
            return new Color(98, 98, 98);
        }

        /**
         * Gets the focus border color depending on button state.
         * @param pComp the component
         * @param pModel the button model
         * @param pSelected the selected
         * @param pFocused the focused
         * @return the focus border color depending on button state.
         */
        protected Color getFocusBorderColor(Component pComp, ButtonModel pModel, boolean pSelected, boolean pFocused) 
        {
            return focusColor;
        }
        
        /**
         * Gets the border color depending on button state.
         * @param pComp the component
         * @param pModel the button model
         * @param pSelected the selected
         * @param pFocused the focused
         * @return the border color depending on button state.
         */
        protected Color getBorderColor(Component pComp, ButtonModel pModel, boolean pSelected, boolean pFocused) 
        {
            return buttonStateColor(pComp, pModel, pFocused,
                    pSelected && selectedBorderColor != null         ? selectedBorderColor : borderColor,
                    pSelected && disabledSelectedBorderColor != null ? disabledSelectedBorderColor : disabledBorderColor,
                    pSelected && focusedSelectedBorderColor != null  ? focusedSelectedBorderColor : focusedBorderColor,
                    pSelected && hoverSelectedBorderColor != null    ? hoverSelectedBorderColor : hoverBorderColor,
                    pSelected && pressedSelectedBorderColor != null  ? pressedSelectedBorderColor : pressedBorderColor);
        }
        
        /**
         * Gets the background depending on button state.
         * @param pComp the component
         * @param pModel the button model
         * @param pSelected the selected
         * @param pFocused the focused
         * @return the background depending on button state.
         */
        protected Color getBackground(Component pComp, ButtonModel pModel, boolean pSelected, boolean pFocused)
        {
            return buttonStateColor(pComp, pModel, pFocused,
                    pSelected && selectedBackground != null         ? selectedBackground : background,
                    pSelected && disabledSelectedBackground != null ? disabledSelectedBackground : disabledBackground,
                    pSelected && focusedSelectedBackground != null  ? focusedSelectedBackground : focusedBackground,
                    pSelected && hoverSelectedBackground != null    ? hoverSelectedBackground : hoverBackground,
                    pSelected && pressedSelectedBackground != null  ? pressedSelectedBackground : pressedBackground);
        }

        /**
         * Gets the check mark color depending on button state.
         * @param pComp the component
         * @param pModel the button model
         * @param pSelected the selected
         * @param pFocused the focused
         * @return the check mark color depending on button state.
         */
        protected Color getCheckmarkColor(Component pComp, ButtonModel pModel, boolean pSelected, boolean pFocused)
        {
            return buttonStateColor(pComp, pModel, pFocused,
                    pSelected || disabledCheckmarkColor == null ? checkmarkColor : disabledCheckmarkColor,
                    disabledCheckmarkColor,
                    focusedCheckmarkColor,
                    hoverCheckmarkColor,
                    pressedCheckmarkColor);
        }
        
        /**
         * Chooses the correct color due to button state.
         * @param pComp the component
         * @param pModel the button model
         * @param pFocused the focused
         * @param pEnabledColor the enabled color
         * @param pDisabledColor the disabled color
         * @param pFocusedColor the focused color
         * @param pHoverColor the hover color
         * @param pPressedColor the pressed color
         * @return the correct color due to button state.
         */
        public static Color buttonStateColor(Component pComp, ButtonModel pModel, boolean pFocused,
                Color pEnabledColor, Color pDisabledColor, Color pFocusedColor, Color pHoverColor, Color pPressedColor)
        {
            if (pComp != null)
            {
                if (pComp.isEnabled())
                {
                    if (pModel != null) 
                    {
                        if (pPressedColor != null && pModel.isPressed())
                        {
                            return pPressedColor;
                        }
                        if (pHoverColor != null && pModel.isRollover())
                        {
                            return pHoverColor;
                        }
                    }
                    if (pFocusedColor != null && pFocused)
                    {
                        return pFocusedColor;
                    }
                }
                else
                {
                    return pDisabledColor;
                }
            }
            return pEnabledColor;
        }
        
        /**
         * Gets whether the component is permanent focus owner. 
         * @param pComp the component
         * @return whether the component is permanent focus owner. 
         */
        public static boolean isPermanentFocusOwner(Component pComp) 
        {
            KeyboardFocusManager keyboardFocusManager = KeyboardFocusManager.getCurrentKeyboardFocusManager();

            if (pComp instanceof JComponent) 
            {
                Object value = ((JComponent)pComp).getClientProperty("JComponent.focusOwner");
                if (value instanceof Predicate) 
                {
                    return ((Predicate<JComponent>)value).test((JComponent)pComp) 
                            && isInActiveWindow(pComp, keyboardFocusManager.getActiveWindow());
                }
            }

            if (pComp.hasFocus())
            {
                return true;
            }

            return keyboardFocusManager.getPermanentFocusOwner() == pComp 
                    && isInActiveWindow(pComp, keyboardFocusManager.getActiveWindow());
        }
        
        /**
         * Gets whether the component is in active window.
         * @param pComp the component
         * @param pActiveWindow the active window
         * @return whether the component is in active window.
         */
        private static boolean isInActiveWindow(Component pComp, Window pActiveWindow) 
        {
            Window window = SwingUtilities.windowForComponent(pComp);
            return window == pActiveWindow 
                    || (window != null && window.getType() == Window.Type.POPUP && window.getOwner() == pActiveWindow);
        }
        
        /**
         * Gets a float from UIManager.
         * @param pKey the key
         * @param pDefaultValue the default value
         * @return a float from UIManager.
         */
        public static float getUIFloat(String pKey, float pDefaultValue) 
        {
            Object value = UIManager.get(pKey);
            
            return (value instanceof Number) ? ((Number)value).floatValue() : pDefaultValue;
        }
        
        /**
         * Gets a color from UIManager.
         * @param pKey the key
         * @param pDefaultColor the default color
         * @return a color from UIManager.
         */
        public static Color getUIColor(String pKey, Color pDefaultColor) 
        {
            Object color = UIManager.getColor(pKey);
            
            return (color instanceof Color) ? (Color)color : pDefaultColor;
        }
	}
	
}	// JVxCheckBox
