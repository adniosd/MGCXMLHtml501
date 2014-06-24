package com.vivogaming.livecasino.screens.choose_table;

import static com.vivogaming.livecasino.global.Constants.API_GET_STATUS;
import static com.vivogaming.livecasino.global.Constants.API_INIT;
import static com.vivogaming.livecasino.global.Constants.ERROR;
import static com.vivogaming.livecasino.global.Constants.GAME_ACTIVITY_FLAG;
import static com.vivogaming.livecasino.global.Dialogs.showDialogLogout;
import static com.vivogaming.livecasino.global.Logout.checkLogoutTime;
import static com.vivogaming.livecasino.global.Logout.doLogout;
import static com.vivogaming.livecasino.global.Logout.setLogoutTime;
import static com.vivogaming.livecasino.global.Variables.getBalance;
import static com.vivogaming.livecasino.global.Variables.getCurrency;
import static com.vivogaming.livecasino.global.Variables.getUserName;
import static com.vivogaming.livecasino.global.Variables.loadVariables;
import static com.vivogaming.livecasino.screens.game.Game.getGameActivity;
import static com.vivogaming.livecasino.screens.game.NotificationDataWorker.getCurrencySymbolByName;
import static com.vivogaming.livecasino.screens.game.ScreenWorker.screenKeep;
import static com.vivogaming.livecasino.web.ResponseWorker.responseApiGetStatusFirst;
import static com.vivogaming.livecasino.web.ResponseWorker.responseApiInit;
import static com.vivogaming.livecasino.web.ResponseWorker.responseError;
import static com.vivogaming.livecasino.web.WebObservable.addMyObserver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.vivogaming.livecasino.R;
import com.vivogaming.livecasino.game_logic.TableObject;
import com.vivogaming.livecasino.screens.choose_table.indicator.CirclePageIndicator;
import com.vivogaming.livecasino.screens.game.Game;
public final class ChooseTable extends Activity implements View.OnClickListener, Observer {
    private TextView tvWelcome_SCT, tvBalance_SCT;
    private Button btnLogout_SCT;
    private ViewPager vpChoose_SCT;

    @Override
    public final void onCreate(final Bundle _savedInstanceState) {
        super.onCreate(_savedInstanceState);
        setContentView(R.layout.screen_choose_table);

        tvWelcome_SCT       = (TextView) findViewById(R.id.tvWelcome_SCT);
        tvBalance_SCT       = (TextView) findViewById(R.id.tvBalance_SCT);
        btnLogout_SCT       = (Button) findViewById(R.id.btnLogout_SCT);
        vpChoose_SCT        = (ViewPager) findViewById(R.id.vp_page_SCT);

        loadVariables(this);

        prepareViewPager();
        prepareWelcomeAndBalanceText();

        btnLogout_SCT.setOnClickListener(this);
    }

    @Override
    public final void onStart() {
        super.onStart();
        Log.d("tag", "ChooseTable: onStart");
        addMyObserver(this, this);
        checkLogoutTime(this);
        RefreshTable.startPeriodicRefresh(this);
        
        /**
         * Keep screen
         * Developer Sam
         * 2014年5月5日
         */
        screenKeep(this);
    }

    @Override
    public final void onStop() {
        super.onStop();
        setLogoutTime(this, System.currentTimeMillis());

        RefreshTable.stopPeriodicRefresh();
    }

    @Override
    public final void onClick(final View _view) {
        if (_view == btnLogout_SCT) {
            onClickBtnLogout_SCT();
        }
    }

    private final void onClickBtnLogout_SCT() {
        doLogout(this, this);
    }

    @Override
    public final void onBackPressed() {
        showDialogLogout(this, this);
    }

    private final void prepareViewPager() {
        final ArrayList<TableObject> tableList = (ArrayList<TableObject>) getIntent().getSerializableExtra("tableList");

        final TableLobbyAdapter pagerAdapter = new TableLobbyAdapter(this, tableList);
        vpChoose_SCT.setAdapter(pagerAdapter);

        final CirclePageIndicator pagerIndicator = (CirclePageIndicator) findViewById(R.id.indicator_SCT);
        pagerIndicator.setViewPager(vpChoose_SCT);
    }


    private final void prepareWelcomeAndBalanceText() {
        final String welcome = getString(R.string.welcome) + " " + getUserName();
        tvWelcome_SCT.setText(welcome);

        final float playerBalance = Float.valueOf(getBalance());
//        tvBalance_SCT.setText(getString(R.string.balance) + " " + playerBalance);
        tvBalance_SCT.setText(getCurrencySymbolByName(getCurrency()) + playerBalance);
    }

    @Override
    public final void update(final Observable _observable, final Object _data) {
        if (_data == null) return;

        final HashMap<String, Object> resultMap = (HashMap<String, Object>) _data;

        /**
         * 
         * Developer Sam
         * 2014年5月16日
         */
        if((Boolean) resultMap.get(GAME_ACTIVITY_FLAG)) return; // && getGameActivity() instanceof Game
        
        if (resultMap.containsKey(ERROR)) {
            final HashMap<String, String> errorMap = (HashMap<String, String>) resultMap.get(ERROR);
            responseError(this, errorMap);
        } else if (resultMap.containsKey(API_INIT)) {
            final HashMap<String, String> initMap = (HashMap<String, String>) resultMap.get(API_INIT);
            responseApiInit(this, initMap);
        } else if (resultMap.containsKey(API_GET_STATUS)) {
            final HashMap<String, String> statusMap = (HashMap<String, String>) resultMap.get(API_GET_STATUS);
            responseApiGetStatusFirst(this, this, statusMap);
        }
    }
}