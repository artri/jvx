/*
 * Copyright 2017 SIB Visions GmbH
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
 * 27.10.2017 - [JR] - creation
 */
package com.sibvisions.util;

import java.math.BigDecimal;

import javax.rad.model.SortDefinition;
import javax.rad.model.condition.Equals;
import javax.rad.model.condition.Or;
import javax.rad.persist.MetaData;

import org.junit.Assert;
import org.junit.Test;

/**
 * Tests functionality of {@link SimpleJavaSource}.
 * 
 * @author René Jahn
 */
public class TestSimpleJavaSource
{
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Test methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Tests {@link SimpleJavaSource#execute(String)}.
     */
    @Test
    public void testExecute()
    {
        SimpleJavaSource ssj = new SimpleJavaSource();
        ssj.addImport("javax.rad.model.condition.*");
        ssj.addImport("javax.rad.model.*");
        ssj.addImport("javax.rad.model.datatype.*");
        ssj.addImport("javax.rad.model.reference.*");
        ssj.addImport("java.math.*");

        String sEqualsCode = "new Equals(\"ID\", BigDecimal.valueOf(0)).or(new Like(\"NAME\", \"%john%\"))";
        
        Assert.assertSame(Or.class, ssj.execute(sEqualsCode).getClass());
        
        String sSortCode = "new SortDefinition(new String[] {\"HAUSNUMMER\", \"STIEGE\"}, new boolean[] {true, false})";
        
        Assert.assertSame(SortDefinition.class, ssj.execute(sSortCode).getClass());
        
        Assert.assertEquals("Hello script!", ssj.execute("\"Hello script!\""));
        Assert.assertEquals(Integer.valueOf(1), ssj.execute("1"));
    }
    
    /**
     * Tests {@link SimpleJavaSource#execute(String)}.
     */
    @Test
    public void testSyntaxFailure()
    {
        SimpleJavaSource ssj = new SimpleJavaSource();
        
        ssj.addImport("javax.rad.model.condition.*");
        ssj.addImport("javax.rad.model.*");
        ssj.addImport("javax.rad.model.datatype.*");
        ssj.addImport("javax.rad.model.reference.*");
        ssj.addImport("java.math.*");

        try
        {
            ssj.execute("new Equals(\"ID\", null");
            Assert.fail("Missing bracket close not detected!");
        }
        catch (Exception ex)
        {
            // ok
        }
        try
        {
            System.out.println(ssj.execute("\"ID"));
            Assert.fail("Missing string close not detected!");
        }
        catch (Exception ex)
        {
            // ok
        }
        try
        {
            System.out.println(ssj.execute("new String[5"));
            Assert.fail("Missing string close not detected!");
        }
        catch (Exception ex)
        {
            // ok
        }
    }
    
    /**
     * Tests instances.
     */
    @Test
    public void testInstances()
    {
        SimpleJavaSource sjs = new SimpleJavaSource();
        sjs.addImport("javax.rad.model.condition.*");
        sjs.addImport("javax.rad.model.*");
        sjs.addImport("javax.rad.model.datatype.*");
        sjs.addImport("javax.rad.model.reference.*");
        sjs.addImport("java.math.*");

        sjs.setFieldValue("c1", new Equals("ID", BigDecimal.valueOf(2)));
        
        String sEqualsCode = "new Equals(\"ID\", BigDecimal.valueOf(0)).or(c1)";
        
        Assert.assertSame(Or.class, sjs.execute(sEqualsCode).getClass());
    }

    /**
     * Tests enums in inner classes.
     */
    @Test
    public void testEnum()
    {
        SimpleJavaSource sjs = new SimpleJavaSource();
        sjs.addImport("javax.rad.persist.*");
        sjs.addImport("javax.rad.persist.MetaData.*");

        Assert.assertEquals(MetaData.Feature.Sort, sjs.execute("Feature.Sort"));

        Assert.assertEquals(MetaData.Feature.Sort, sjs.execute("MetaData.Feature.Sort"));
    }

    /**
     * Tests operators.
     */
    @Test
    public void testOperator()
    {
        SimpleJavaSource sjs = new SimpleJavaSource();

        Assert.assertEquals(Double.valueOf(10.4d), sjs.execute("1 + 2 + (12 / 5d) + 5"));
        Assert.assertEquals("Hallo Martin", sjs.execute("\"Hallo\" + \" Martin\""));
    }

    /**
     * Tests operators.
     */
    @Test
    public void testClassField()
    {
        SimpleJavaSource sjs = new SimpleJavaSource();

        Assert.assertEquals(String.class, sjs.execute("String.class"));

        Assert.assertEquals(String.class.getName(), sjs.execute("String.class.getName()"));
        
        Assert.assertEquals(String.class.getSimpleName(), sjs.execute("String.class.getSimpleName()"));      
    }

    /**
     * Tests operators.
     */
    @Test
    public void testParseString()
    {
        SimpleJavaSource sjs = new SimpleJavaSource();

        Assert.assertEquals("JJCDEF", sjs.parseString("\\u004a\\u004A\\u0043\\u0044\\u0045\\u0046"));
        Assert.assertEquals("ABCDEF", sjs.parseString("\\uuuu0041\\u0042\\u0043\\u0044\\u0045\\u0046"));
        Assert.assertEquals("ABCD\nEFHallo \\ \r \t \nHallo", sjs.parseString("\\u0041\\u0042\\u0043\\u0044\\n\\u0045\\u0046Hallo \\ \\r \\t \\nHallo"));

        Assert.assertEquals("package.Function(\"Hallo\");\nTest", sjs.parseString("package.Function(\\\"Hallo\\\");\\nTest"));

        Assert.assertEquals("\\u23", sjs.parseString("\\u23"));
        Assert.assertEquals("\\", sjs.parseString("\\"));
    }
    
    /**
     * Tests code injection.
     */
    @Test
    public void testInjection()
    {
        SimpleJavaSource sjs = new SimpleJavaSource();
        
        SecurityManager sman = new SecurityManager();
        
        try
        {
        	sjs.execute("org.junit.Assert.fail(\"Should not work!\")");
        }
        catch (Exception e)
        {
        	Assert.assertEquals("Should not work!", e.getCause().getMessage());
        }
    }

    /**
     * Tests System.out.
     */
    @Test
    public void testSystemOut()
    {
        SimpleJavaSource ssj = new SimpleJavaSource();
        ssj.execute("System.out.println(\"x\")");
    }

    /**
     * Tests the security.
     */
    @Test
    public void testSecurity()
    {
        SimpleJavaSource ssj = new SimpleJavaSource();
        ssj.addImport("javax.rad.model.condition.*");
        ssj.addImport("javax.rad.model.*");
        ssj.addImport("javax.rad.model.datatype.*");
        ssj.addImport("javax.rad.model.reference.*");
        ssj.addImport("com.sibvisions.rad.model.mem.*");
        ssj.addImport("java.math.*");

        ssj.addAllowedMethod("ICondition.*");
        ssj.addAllowedDerivedConstructors("ICondition");
        ssj.addAllowedConstructor("DataRow");
        
        ssj.addAllowedMethod("System.*");
        ssj.addDeniedMethod("System.exit");
        
        // This is allowed
        ssj.execute("new Equals(\"ID\", \"15\").isFulfilled(new DataRow())");
        // This is allowed
        ssj.execute("System.currentTimeMillis()");
        // Constructor of derived classes is allowed
        ssj.execute("new ContainsIgnoreCase(\"TEXT\", \"*Hallo*\").isFulfilled(new DataRow())");

        try
        {
        	ssj.execute("System.exit(0)");
        	
        	Assert.fail("System exit is forbidden!");
        }
        catch (Exception ex)
        {
            System.out.println(ex.getMessage());
        }
        try
        {
        	ssj.execute("new DataRow().getValue(\"ID\")");

        	Assert.fail("DataRow getValue is forbidden!");
        }
        catch (Exception ex)
        {
            System.out.println(ex.getMessage());
        }
       
    }

    
}   // TestSimpleJavaSource
