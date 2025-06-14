module library_management_system {
	requires javafx.controls;
	requires javafx.fxml;
	
	requires java.sql;
 
	
	opens application to javafx.graphics, javafx.fxml;
}
