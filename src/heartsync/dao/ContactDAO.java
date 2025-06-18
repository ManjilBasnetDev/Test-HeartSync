package heartsync.dao;

import heartsync.model.Contact;
import heartsync.database.FirebaseConfig;
import com.google.gson.reflect.TypeToken;
import java.util.Map;
import java.util.UUID;
import java.io.IOException;

public class ContactDAO {
    private static final String CONTACTS_PATH = "contacts";

    public boolean saveContact(Contact contact) {
        try {
            // Generate a unique ID for the contact
            String contactId = UUID.randomUUID().toString();
            
            // Get existing contacts
            Map<String, Contact> contacts = FirebaseConfig.get(CONTACTS_PATH, 
                new TypeToken<Map<String, Contact>>(){}.getType());
            
            if (contacts == null) {
                contacts = new java.util.HashMap<>();
            }
            
            // Add the new contact
            contacts.put(contactId, contact);
            
            // Save to Firebase
            FirebaseConfig.set(CONTACTS_PATH, contacts);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Map<String, Contact> getAllContacts() {
        try {
            return FirebaseConfig.get(CONTACTS_PATH, 
                new TypeToken<Map<String, Contact>>(){}.getType());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
} 