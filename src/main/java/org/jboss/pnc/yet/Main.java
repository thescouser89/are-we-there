package org.jboss.pnc.yet;

public class Main {
    public static void main(String[] args) throws Exception {
        GitClient gitClient = new GitClient("https://github.com/project-ncl/repour");
        System.out.println(gitClient.getParentOfCommit("pnc-2.6.0"));
        System.out.println(gitClient.getCommiterOfCommit("pnc-2.6.0"));
        System.out.println(gitClient.getMessageOfCommit("pnc-2.6.0"));
        System.out.println(gitClient.isCommitPresent("pnc-2.5.1"));
    }
}