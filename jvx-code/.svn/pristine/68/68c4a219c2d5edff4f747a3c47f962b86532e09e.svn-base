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
 * 25.10.2017 - [JR] - creation
 */
import javax.rad.model.SortDefinition;
import javax.rad.model.condition.Or;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import org.junit.Assert;
import org.junit.Test;

/**
 * Tests functionality of {@link ScriptEngine}.
 * 
 * @author René Jahn
 */
public class TestScriptEngine
{
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Test methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    /**
     * Tests {@link ScriptEngine#eval(String)}.
     * 
     * @throws Exception if test fails
     */
    @Test
    public void testEval() throws Exception
    {
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("JavaScript");

        String sEqualsCode = "new Equals(\"ID\", BigDecimal.valueOf(0)).or(new Like(\"NAME\", \"%jahn%\"))";
        String sSortCode = "new SortDefinition(new String[] {\"HAUSNUMMER\", \"STIEGE\"}, new boolean[] {true, false})";
        
        StringBuilder sbImports = new StringBuilder();
        sbImports.append("var jvx = new JavaImporter(javax.rad.model.condition, javax.rad.model, javax.rad.model.dataType, java.math, java.sql, java.lang);");
        sbImports.append("with (jvx)");
        sbImports.append("{");
        sbImports.append(sEqualsCode);
        sbImports.append("}");
        
        Assert.assertSame(Or.class, engine.eval(sbImports.toString()).getClass());
        
        Assert.assertEquals("12", engine.eval("\"12\""));

        sbImports.setLength(0);
        sbImports.append("var jvx = new JavaImporter(javax.rad.model.condition, javax.rad.model, javax.rad.model.dataType, java.math, java.sql, java.lang);");
        sbImports.append("with (jvx)");
        sbImports.append("{");
        sbImports.append(sSortCode);
        sbImports.append("}");

        try
        {
            //wrong array syntax, see: https://docs.oracle.com/javase/7/docs/technotes/guides/scripting/programmer_guide/
            Assert.assertSame(SortDefinition.class, engine.eval(sbImports.toString()).getClass());
            
            Assert.fail("Java String[] created with wrong JavaScript syntax!");
        }
        catch (Exception e)
        {
            //expected
        }        
    }
    
}   // TestScriptEngine
