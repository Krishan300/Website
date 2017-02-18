package edu.lehigh.luna.web;

import static spark.Spark.*;
/**
 * Hello world!
 *
 */
public class App 
{
    public static void main(String[] args) {
        get("/hello", (req,res) -> {
            return "Hello Word";
        });
        staticFileLocation("/web");

        get("/", (req, res) -> {
            res.redirect("/index.html");
            return "";
        });
    }
}
