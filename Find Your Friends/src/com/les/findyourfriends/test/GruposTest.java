package com.les.findyourfriends.test;

import com.robotium.solo.*;
import android.test.ActivityInstrumentationTestCase2;


@SuppressWarnings("rawtypes")
public class GruposTest extends ActivityInstrumentationTestCase2 {
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
    public GruposTest() throws ClassNotFoundException {
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
		// Click on Criar grupo
		solo.clickOnView(solo.getView("criarGrupo"));
		// Wait for activity: 'com.findyourfriends.activitys.CriarGrupoActivity'
		assertTrue("CriarGrupoActivity is not found!", solo.waitForActivity("CriarGrupoActivity"));
		// Click on Empty Text View
		solo.clickOnView(solo.getView("edNomeGrupo"));
		// Enter the text: 'les'
		solo.clearEditText((android.widget.EditText) solo.getView("edNomeGrupo"));
		solo.enterText((android.widget.EditText) solo.getView("edNomeGrupo"), "les");
		// Click on Empty Text View
		solo.clickOnView(solo.getView("edSenha"));
		// Enter the text: 'les'
		solo.clearEditText((android.widget.EditText) solo.getView("edSenha"));
		solo.enterText((android.widget.EditText) solo.getView("edSenha"), "les");
		// Click on Salvar
		solo.clickOnView(solo.getView("salvar"));
		// Wait for activity: 'com.findyourfriends.activitys.MeusGruposActivity'
		assertTrue("MeusGruposActivity is not found!", solo.waitForActivity("MeusGruposActivity"));
		// Click on les 4
		solo.clickInList(3, 0);
		// Wait for activity: 'com.findyourfriends.activitys.GrupoActivity'
		assertTrue("GrupoActivity is not found!", solo.waitForActivity("GrupoActivity"));
		// Click on Visualizar mapa
		solo.clickOnView(solo.getView("visualizarMapa"));
		// Wait for activity: 'com.findyourfriends.activitys.Map'
		assertTrue("Map is not found!", solo.waitForActivity("Map"));
		// Click on ovw
		solo.clickOnView(solo.getView(android.view.TextureView.class, 0));
		// Click on View
		solo.clickOnView(solo.getView(0x2));
		// Click on View
		solo.clickOnView(solo.getView(android.view.View.class, 12));
		// Press menu back key
		solo.goBack();
		// Press menu back key
		solo.goBack();
		// Press menu back key
		solo.goBack();
		// Click on ImageView
		solo.clickOnView(solo.getView("meusGrupos"));
		// Wait for activity: 'com.findyourfriends.activitys.MeusGruposActivity'
		assertTrue("MeusGruposActivity is not found!", solo.waitForActivity("MeusGruposActivity"));
		// Click on Renan 1
		solo.clickInList(1, 0);
		// Wait for activity: 'com.findyourfriends.activitys.GrupoActivity'
		assertTrue("GrupoActivity is not found!", solo.waitForActivity("GrupoActivity"));
		// Scroll to 37467878
		android.widget.ListView listView0 = (android.widget.ListView) solo.getView(android.widget.ListView.class, 0);
		solo.scrollListToLine(listView0, 0);
		// Press menu back key
		solo.goBack();
		// Click on ImageView
		solo.clickOnView(solo.getView("grupos"));
		// Wait for activity: 'com.findyourfriends.activitys.ViewGroupActivity'
		assertTrue("ViewGroupActivity is not found!", solo.waitForActivity("ViewGroupActivity"));
		// Click on Giovana 3
		solo.clickInList(2, 0);
		// Wait for activity: 'com.findyourfriends.activitys.EntraNoGrupo'
		assertTrue("EntraNoGrupo is not found!", solo.waitForActivity("EntraNoGrupo"));
		// Press menu back key
		solo.goBack();
		// Click on ImageView
		solo.clickOnView(solo.getView("editar"));
		// Wait for activity: 'com.findyourfriends.activitys.EditarActivity'
		assertTrue("EditarActivity is not found!", solo.waitForActivity("EditarActivity"));
		// Click on les 4
		solo.clickInList(3, 0);
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
		// Press menu back key
		solo.goBack();
	}
}
