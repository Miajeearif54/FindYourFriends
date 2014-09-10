package com.les.findyourfriends.test;

import com.robotium.solo.*;

import android.test.ActivityInstrumentationTestCase2;


@SuppressWarnings("rawtypes")
public class TestPrincipal extends ActivityInstrumentationTestCase2 {
  	private Solo solo;
  	
  	private static final String LAUNCHER_ACTIVITY_FULL_CLASSNAME = "com.findyourfriends.activitys.Splash";

    private static Class<?> launcherActivityClass;
    static{
        try {
            launcherActivityClass = Class.forName(LAUNCHER_ACTIVITY_FULL_CLASSNAME);
        } catch (ClassNotFoundException e) {
           throw new RuntimeException(e);
        }
    }
  	
  	@SuppressWarnings("unchecked")
    public TestPrincipal() throws ClassNotFoundException {
        super(launcherActivityClass);
    }

  	public void setUp() throws Exception {
        super.setUp();
		solo = new Solo(getInstrumentation());
		getActivity();
  	}
  
   	@Override
   	public void tearDown() throws Exception {
        solo.finishOpenedActivities();
        super.tearDown();
  	}
   	
   	
   	public void login(){
   	 solo.waitForActivity("Splash", 2000);
   	 solo.clickOnView(solo.getView("buttonContinuar"));
   	}
   	

   	public void testLogin() {
        login();
        assertTrue("Map is not found!", solo.waitForActivity("Map"));
   	}

   	public void testCriarGrupo(){
        login();

        solo.clickOnView(solo.getView("editar"));
        assertTrue("EditarActivity is not found!", solo.waitForActivity("EditarActivity"));
        solo.waitForDialogToOpen(6000);

        solo.clickOnView(solo.getView("criarGrupo"));
        assertTrue("CriarGrupoActivity is not found!", solo.waitForActivity("CriarGrupoActivity"));

        solo.clickOnView(solo.getView("edNomeGrupo"));

        // Enter the text: 'testegrupo '
        solo.enterText((android.widget.EditText) solo.getView("edNomeGrupo"), "robotium ");
        // Click on Empty Text View
        solo.enterText((android.widget.EditText) solo.getView("edSenha"), "123");

        solo.clickOnView(solo.getView("salvar"));
        solo.goBack();
   	}
   	
   	public void testEntrarGrupo(){
        login();
   	    
   	    solo.clickOnView(solo.getView("grupos"));
        assertTrue("ViewGroupActivity is not found!", solo.waitForActivity("ViewGroupActivity"));
        solo.waitForDialogToOpen(6000);
        
        solo.clickInList(6, 0);
        assertTrue("EntraNoGrupo is not found!", solo.waitForActivity("EntraNoGrupo"));
        
        solo.clickOnView(solo.getView("edSenha_entrar"));
        solo.enterText((android.widget.EditText) solo.getView("edSenha_entrar"), "renan");
        

        solo.clickOnView(solo.getView("entrar"));
        assertTrue("MeusGruposActivity is not found!", solo.waitForActivity("MeusGruposActivity"));
        solo.goBack();
   	}
   	
   	
   	/*public void testApagarGrupo(){
   	    login();

        solo.clickOnView(solo.getView("editar"));
        assertTrue("EditarActivity is not found!", solo.waitForActivity("EditarActivity"));

        solo.clickInList(2, 0);
        solo.waitForDialogToOpen(6000);
        solo.clickOnView(solo.getView(android.R.id.button1));

        assertTrue("Map is not found!", solo.waitForActivity("Map"));
   	}*/
   	
   	
   	public void testDeslogar(){
   	    login();

        solo.goBack();
        solo.waitForDialogToOpen(8000);
        solo.clickOnView(solo.getView("btn_sign_out"));
        
        solo.waitForActivity("MainActivity", 4000);
   	}
  
	public void testRun() {
	    // Wait for activity: 'com.findyourfriends.activitys.Splash'
        solo.waitForActivity("Splash", 2000);
        // Wait for activity: 'com.findyourfriends.activitys.MainActivity'
        assertTrue("MainActivity is not found!", solo.waitForActivity("MainActivity"));
        // Click on Continuar
        solo.clickOnView(solo.getView("buttonContinuar"));
        // Wait for activity: 'com.findyourfriends.activitys.Map'
        assertTrue("Map is not found!", solo.waitForActivity("Map"));
        // Click on ImageView
        solo.clickOnView(solo.getView("editar"));
        // Wait for activity: 'com.findyourfriends.activitys.EditarActivity'
        assertTrue("EditarActivity is not found!", solo.waitForActivity("EditarActivity"));
        // Set default small timeout to 11820 milliseconds
        Timeout.setSmallTimeout(11820);
        // Click on Criar grupo
        solo.clickOnView(solo.getView("criarGrupo"));
        // Wait for activity: 'com.findyourfriends.activitys.CriarGrupoActivity'
        assertTrue("CriarGrupoActivity is not found!", solo.waitForActivity("CriarGrupoActivity"));
        // Click on Empty Text View
        solo.clickOnView(solo.getView("edNomeGrupo"));
        // Set default small timeout to 19987 milliseconds
        Timeout.setSmallTimeout(19987);
        // Enter the text: 'testegrupo '
        solo.clearEditText((android.widget.EditText) solo.getView("edNomeGrupo"));
        solo.enterText((android.widget.EditText) solo.getView("edNomeGrupo"), "testegrupo ");
        // Click on Empty Text View
        solo.clickOnView(solo.getView("edSenha"));
        // Enter the text: '123'
        solo.clearEditText((android.widget.EditText) solo.getView("edSenha"));
        solo.enterText((android.widget.EditText) solo.getView("edSenha"), "123");
        // Click on Salvar
        solo.clickOnView(solo.getView("salvar"));
        // Press menu back key
        solo.goBack();
        // Click on ImageView
        solo.clickOnView(solo.getView("editar"));
        // Wait for activity: 'com.findyourfriends.activitys.EditarActivity'
        assertTrue("EditarActivity is not found!", solo.waitForActivity("EditarActivity"));
        // Click on ImageView
        solo.clickOnView(solo.getView("grupos"));
        // Wait for activity: 'com.findyourfriends.activitys.ViewGroupActivity'
        assertTrue("ViewGroupActivity is not found!", solo.waitForActivity("ViewGroupActivity"));
        // Click on ImageView
        solo.clickOnView(solo.getView("editar"));
        // Wait for activity: 'com.findyourfriends.activitys.EditarActivity'
        assertTrue("EditarActivity is not found!", solo.waitForActivity("EditarActivity"));
        // Click on Criar grupo
        solo.clickOnView(solo.getView("criarGrupo"));
        // Wait for activity: 'com.findyourfriends.activitys.CriarGrupoActivity'
        assertTrue("CriarGrupoActivity is not found!", solo.waitForActivity("CriarGrupoActivity"));
        // Click on Empty Text View
        solo.clickOnView(solo.getView("edNomeGrupo"));
        // Enter the text: 'testegrupo'
        solo.clearEditText((android.widget.EditText) solo.getView("edNomeGrupo"));
        solo.enterText((android.widget.EditText) solo.getView("edNomeGrupo"), "testegrupo");
        // Click on Empty Text View
        solo.clickOnView(solo.getView("edSenha"));
        // Enter the text: '12345'
        solo.clearEditText((android.widget.EditText) solo.getView("edSenha"));
        solo.enterText((android.widget.EditText) solo.getView("edSenha"), "12345");
        // Click on Salvar
        solo.clickOnView(solo.getView("salvar"));
        // Press menu back key
        solo.goBack();
        // Click on ImageView
        solo.clickOnView(solo.getView("grupos"));
        // Wait for activity: 'com.findyourfriends.activitys.ViewGroupActivity'
        assertTrue("ViewGroupActivity is not found!", solo.waitForActivity("ViewGroupActivity"));
        // Click on testegrupo 13
        solo.clickInList(5, 0);
        // Wait for activity: 'com.findyourfriends.activitys.EntraNoGrupo'
        assertTrue("EntraNoGrupo is not found!", solo.waitForActivity("EntraNoGrupo"));
        // Click on Empty Text View
        solo.clickOnView(solo.getView("edSenha_entrar"));
        // Enter the text: '12345'
        solo.clearEditText((android.widget.EditText) solo.getView("edSenha_entrar"));
        solo.enterText((android.widget.EditText) solo.getView("edSenha_entrar"), "12345");
        // Click on Entrar no Grupo
        solo.clickOnView(solo.getView("entrar"));
        // Wait for activity: 'com.findyourfriends.activitys.MeusGruposActivity'
        assertTrue("MeusGruposActivity is not found!", solo.waitForActivity("MeusGruposActivity"));
        // Click on ImageView
        solo.clickOnView(solo.getView("grupos"));
        // Wait for activity: 'com.findyourfriends.activitys.ViewGroupActivity'
        assertTrue("ViewGroupActivity is not found!", solo.waitForActivity("ViewGroupActivity"));
        // Click on renanpintos 11
        solo.clickInList(3, 0);
        // Wait for activity: 'com.findyourfriends.activitys.EntraNoGrupo'
        assertTrue("EntraNoGrupo is not found!", solo.waitForActivity("EntraNoGrupo"));
        // Click on Empty Text View
        solo.clickOnView(solo.getView("edSenha_entrar"));
        // Enter the text: 'agaggdjdd'
        solo.clearEditText((android.widget.EditText) solo.getView("edSenha_entrar"));
        solo.enterText((android.widget.EditText) solo.getView("edSenha_entrar"), "agaggdjdd");
        // Click on Entrar no Grupo
        solo.clickOnView(solo.getView("entrar"));
        // Press menu back key
        solo.goBack();
        // Press menu back key
        solo.goBack();
        // Click on View
        solo.clickOnView(solo.getView(0x2));
        // Click on ImageView
        solo.clickOnView(solo.getView("editar"));
        // Wait for activity: 'com.findyourfriends.activitys.EditarActivity'
        assertTrue("EditarActivity is not found!", solo.waitForActivity("EditarActivity"));
        // Click on testegrupo 13
        solo.clickInList(2, 0);
        // Wait for dialog
        solo.waitForDialogToOpen(5000);
        // Click on OK
        solo.clickOnView(solo.getView(android.R.id.button1));
        // Wait for activity: 'com.findyourfriends.activitys.Map'
        assertTrue("Map is not found!", solo.waitForActivity("Map"));
        // Press menu back key
        solo.goBack();
        // Press menu back key
        solo.goBack();
        // Click on Sair do Google
        solo.clickOnView(solo.getView("btn_sign_out"));
        // Click on Login
        solo.clickOnText(java.util.regex.Pattern.quote("Login"));
        // Click on action bar item
        solo.clickOnActionBarItem(0x7f060031);
        // Click on Login
        solo.clickOnText(java.util.regex.Pattern.quote("Login"));
        // Click on Continuar
        solo.clickOnView(solo.getView("buttonContinuar"));
        // Wait for activity: 'com.findyourfriends.activitys.Map'
        assertTrue("Map is not found!", solo.waitForActivity("Map"));
        // Click on ovw
        solo.clickOnView(solo.getView(android.view.TextureView.class, 0));
        // Click on View
        solo.clickOnView(solo.getView(android.view.View.class, 16));
        // Click on ovw
        solo.clickOnView(solo.getView(android.view.TextureView.class, 0));
        // Click on ovw
        solo.clickOnView(solo.getView(android.view.TextureView.class, 0));
        // Click on View
        solo.clickOnView(solo.getView(0x2));
        // Click on ImageView
        solo.clickOnView(solo.getView("meusGrupos"));
        // Wait for activity: 'com.findyourfriends.activitys.MeusGruposActivity'
        assertTrue("MeusGruposActivity is not found!", solo.waitForActivity("MeusGruposActivity"));
        // Wait for dialog
        solo.waitForDialogToOpen(5000);
        // Set default small timeout to 40216 milliseconds
        Timeout.setSmallTimeout(40216);
        // Press menu back key
        solo.goBack();
	}
}
