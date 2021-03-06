package com.example.rajesh.expensetracker;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.rajesh.expensetracker.account.list.AccountFragment;
import com.example.rajesh.expensetracker.base.activity.BaseActivity;
import com.example.rajesh.expensetracker.category.AddCategoryFragment;
import com.example.rajesh.expensetracker.category.CategoryFragment;
import com.example.rajesh.expensetracker.category.CategoryLongPressListener;
import com.example.rajesh.expensetracker.category.ExpenseCategory;
import com.example.rajesh.expensetracker.dashboard.DashBoardFragment;
import com.example.rajesh.expensetracker.dashboard.Expense;
import com.example.rajesh.expensetracker.expense.ExpenseFragment;
import com.example.rajesh.expensetracker.expense.ExpenseLongPressListener;
import com.example.rajesh.expensetracker.expense.recurring.RecurringFragment;
import com.example.rajesh.expensetracker.notification.PollingService;
import com.example.rajesh.expensetracker.report.ReportFragment;

public class DashboardActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener, CategoryLongPressListener, ExpenseLongPressListener {

    Toolbar toolbar;
    String toolbarTitle = null;

    private static final String ACCOUNTS_TITLE = "Accounts";
    private static final String CATEGORIES_TITLE = "Categories";
    private static final String HISTORY_AND_REPORT_TITLE = "History / Report";
    private static final String RECURRING_EXPENSE_TITLE = "Recurring Expense";
    private static final String DASHBOARD_TITLE = "Dashboard";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //startActivity(new Intent(this, ConfirmationActivity.class));

        bindView();
        setSupportActionBar(toolbar);
        setNavigationDrawer();
        addFragment(new DashBoardFragment(), Constant.FragmentTag.DASHBOARD_FRAGMENT_TAG);

        startService(new Intent(this, PollingService.class));
    }

    private void setNavigationDrawer() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void bindView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
    }


    @Override
    protected int getLayout() {
        return R.layout.activity_navigation;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.navigation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        Fragment fragment = null;
        String fragmentTag = null;

        switch (id) {
            case R.id.nav_account:
                fragment = new AccountFragment();
                fragmentTag = "NewFragment";
                toolbarTitle = ACCOUNTS_TITLE;
                break;
            case R.id.nav_categories:
                fragment = new CategoryFragment();
                fragmentTag = Constant.FragmentTag.CATEGORY_FRAGMENT;
                toolbarTitle = CATEGORIES_TITLE;
                break;
            case R.id.nav_history_report:
                fragment = new ReportFragment();
                fragmentTag = Constant.FragmentTag.REPORT_FRAGMENT;
                toolbarTitle = HISTORY_AND_REPORT_TITLE;
                break;
            case R.id.nav_recurring_expense:
                fragment = new RecurringFragment();
                fragmentTag = Constant.FragmentTag.EXPENSE_FRAGMENT;
                toolbarTitle = RECURRING_EXPENSE_TITLE;
                break;
            case R.id.nav_settings:
                break;
            case R.id.nav_dashboard:
                fragment = new DashBoardFragment();
                fragmentTag = Constant.FragmentTag.DASHBOARD_FRAGMENT_TAG;
                toolbarTitle = DASHBOARD_TITLE;
                break;
            default:
                break;
        }
        addFragment(fragment, fragmentTag);
        getSupportActionBar().setTitle(toolbarTitle);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onCategoryLongPress(ExpenseCategory expenseCategory) {
        AddCategoryFragment addCategoryFragment = AddCategoryFragment.getInstance(expenseCategory);
        addFragment(addCategoryFragment, Constant.FragmentTag.CATEGORY_FRAGMENT);
    }


    private void addFragment(Fragment fragment, String tag) {
        getSupportFragmentManager().beginTransaction().replace(R.id.ll_dashboard_wrapper, fragment, tag).commit();
    }

    @Override
    public void onExpenseLongPress(Expense expense, ExpenseCategory expenseCategory) {
        ExpenseFragment expenseFragment = ExpenseFragment.getInstance(null, null);
        addFragment(expenseFragment, Constant.FragmentTag.EXPENSE_FRAGMENT);
    }

    public static Intent getLaunchIntent(Context context) {
        Intent intent = new Intent(context, DashboardActivity.class);
        return intent;
    }
}
