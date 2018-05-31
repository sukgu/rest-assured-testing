package com.framework.utils;


import java.util.LinkedList;
import java.util.List;

import org.junit.AssumptionViolatedException;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

public class TestWatcherLoggingRule extends TestWatcher {

    protected static List<Description> failed=new LinkedList<Description>();
    protected static List<Description> passed=new LinkedList<Description>();
	
	@Override
	public Statement apply(Statement base, Description description) {
		// TODO Auto-generated method stub
		
		return super.apply(base, description);
	}

	@Override
	protected void failed(Throwable e, Description description) {
		// TODO Auto-generated method stub
		failed.add(description);
		LogUtil.error("[FAILED]  "+description.getMethodName()+" [Test Failed] "+e.getMessage());
		super.failed(e, description);
	}

	@Override
	protected void finished(Description description) {
		// TODO Auto-generated method stub
		LogUtil.info("[FINISHED] "+description.getMethodName());
		super.finished(description);
	}

	@Override
	protected void skipped(AssumptionViolatedException e,
			Description description) {
		LogUtil.error("[FAILED] Test Failed due to Assumption Voilation "+e.getMessage() );
		super.skipped(e, description);
	}

	@Override
	protected void starting(Description description) {
		// TODO Auto-generated method stub
		LogUtil.info("------------------------------------------------------------");
		LogUtil.info("[STARTED]  "+description.getMethodName());
		super.starting(description);
	}

	@Override
	protected void succeeded(Description description) {
		// TODO Auto-generated method stub
		passed.add(description);
		LogUtil.info("[PASSED]   "+description.getMethodName());
		super.succeeded(description);
	}

}
