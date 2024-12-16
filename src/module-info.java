module CaLouseIF {
	requires javafx.graphics;
	requires java.sql;
	requires javafx.controls;
	
	exports main;
	exports model;
	
	opens main to javafx.graphics;
}