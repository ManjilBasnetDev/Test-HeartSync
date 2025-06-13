/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit4TestClass.java to edit this template
 */
package heartsync.dao;

import heartsync.model.Contact;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Edsha
 */
public class ContactDAOTest {
    
   @Test
   public void ContactWithNewDetails(){
       String correctUsername = "TestName";
       String correctPassword = "Test@User1";
       @Test
       public void ContactWithNewDetails(){
       UserData user = new UserData(correctUsername,correctPassword);
       boolean result = dao.contact(user);
       Assert.assertTrue("Contact should work with unique details" ,result);
       }
       public void ContactWithDuplicateDetails(){
           UserData user = new UserData (correctUsername,correctPassword);
           boolean result = dao. register(user);
           Assert.assertFalse("Contact should fail with duplicate credentials",result);
            }
       @Test
       public void loginWithCorrectCreds(){
           LoginRequest req = new LoginRequest("abc@test.com",)
       }
       
   }
}
