package com.oldsugar.pattern.model.product;

import com.oldsugar.pattern.dao.impl.FlyImpl;
import com.oldsugar.pattern.dao.impl.QuackImpl;
import com.oldsugar.pattern.model.Duck;

public class ModelDuck extends Duck{
	public ModelDuck() {
		quackBehavior = new QuackImpl();
		flyBehavior = new FlyImpl();
	}

	@Override
	public void display() {
		System.out.println("I M NEW duck!!!");		
	}

}
