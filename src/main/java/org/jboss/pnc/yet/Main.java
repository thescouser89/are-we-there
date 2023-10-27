package org.jboss.pnc.yet;

public class Main {
    public static void main(String[] args) throws Exception {

        System.out.println(GitFinder.find("https://github.com/project-ncl/repour", null, "pnc-2.6.0"));
        System.out.println(GitFinder.find("https://github.com/project-ncl/repour", null, "pnc-2.5.1"));
        System.out.println(GitFinder.find("https://github.com/project-ncl/repour", null, "pnc-2.5.0"));

        GitFinder.cleanup();
    }
}