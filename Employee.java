import java.time.LocalDate;

public class Employee {
    
    String emp_id;
    String project_id;
    String date_from;
    String date_to;

    
    /* constructor for the Employee objects 
     * sets all attributes
    */
    public Employee(String emp_id, String project_id, String date_from, String date_to){
        this.emp_id = emp_id;
        this.project_id = project_id;
        this.date_from = date_from;
        this.date_to = date_to;
    }


    /* getter for the employee id as an integger */
    public int get_id(){
        return Integer.parseInt(this.emp_id);
    }


    /* getter for the project id as an integger */
    public int get_project_id(){
        return Integer.parseInt(this.project_id);
    }


    /* getter for the start date parsed as LocalDate 
     * return todays date if the input start date is null
    */
    public LocalDate get_start(){
        LocalDate start;
        this.date_from = this.date_from.trim();
        if(this.date_from.equals("NULL")){
            start = LocalDate.now();
        }else{
            start = LocalDate.parse(this.date_from);
        }
        return start;
    }


    /* getter for the end date parsed as LocalDate 
     * return todays date if the input end date is null
    */
    public LocalDate get_end(){
        LocalDate end;
        this.date_to = this.date_to.trim();
        if(this.date_to.equals("NULL")){
            end = LocalDate.now();
        }else{
            end = LocalDate.parse(this.date_to);
        }
        return end;
    } 
}