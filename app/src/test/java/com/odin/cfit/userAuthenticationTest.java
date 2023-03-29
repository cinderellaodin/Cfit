package com.odin.cfit;



import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.junit.Test;
import org.mockito.Mock;

public class userAuthenticationTest {
    //login activity = new login();

    @Mock
    FirebaseAuth firebaseAuthMock = mock(FirebaseAuth.class);

    @Test
    public void testUserRegistration() {
        String email = "testuser@example.com";
        String password = "testpassword";

        Task<AuthResult> successfulTask = mock(Task.class);
        when(successfulTask.isSuccessful()).thenReturn(true);

        Task<AuthResult> failedTask = mock(Task.class);
        when(failedTask.isSuccessful()).thenReturn(false);

        when(firebaseAuthMock.createUserWithEmailAndPassword(email, password))
                .thenReturn(successfulTask)
                .thenReturn(failedTask);

        // Test successful registration
        assertTrue(firebaseAuthMock.createUserWithEmailAndPassword(email, password).isSuccessful());

        // Test failed registration
        assertFalse(firebaseAuthMock.createUserWithEmailAndPassword(email, "testpassword").isSuccessful());
    }

    @Test
    public void testUserLogin() {
        String email = "testuser@example.com";
        String password = "testpassword";

        Task<AuthResult> successfulTask = mock(Task.class);
        when(successfulTask.isSuccessful()).thenReturn(true);

        Task<AuthResult> failedTask = mock(Task.class);
        when(failedTask.isSuccessful()).thenReturn(false);

        when(firebaseAuthMock.signInWithEmailAndPassword(email, password))
                .thenReturn(successfulTask)
                .thenReturn(failedTask);

        // Test successful login
        assertTrue(firebaseAuthMock.signInWithEmailAndPassword(email, password).isSuccessful());

        // Test failed login
        assertFalse(firebaseAuthMock.signInWithEmailAndPassword(email, "wrongpassword").isSuccessful());
    }



    @Mock
    FirebaseUser firebaseUserMock = mock(FirebaseUser.class);

    @Test
    public void testUserSignOut() {
        when(firebaseAuthMock.getCurrentUser())
                .thenReturn(firebaseUserMock)
                .thenReturn(null);

        // Test successful sign out
        assertTrue(firebaseAuthMock.getCurrentUser() != null);
        firebaseAuthMock.signOut();
        assertTrue(firebaseAuthMock.getCurrentUser() == null);
    }
}