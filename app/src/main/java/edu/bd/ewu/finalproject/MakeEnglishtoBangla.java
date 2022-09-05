package edu.bd.ewu.finalproject;

public class MakeEnglishtoBangla {
    public String make(String number){
        String result = "";
        for (int i = 0; i < number.length(); i++) {
            if(number.charAt(i) == '0') result += '০';
            else if(number.charAt(i) == '1') result += '১';
            else if(number.charAt(i) == '2') result += '২';
            else if(number.charAt(i) == '3') result += '৩';
            else if(number.charAt(i) == '4') result += '৪';
            else if(number.charAt(i) == '5') result += '৫';
            else if(number.charAt(i) == '6') result += '৬';
            else if(number.charAt(i) == '7') result += '৭';
            else if(number.charAt(i) == '8') result += '৮';
            else if(number.charAt(i) == '9') result += '৯';
            else result += number.charAt(i);
        }
        return result;
    }
    public String makeReverse(String number){
        String result = "";
        for (int i = 0; i < number.length(); i++) {
            if(i == '০') result += '0';
            else if(number.charAt(i) == '১') result += '1';
            else if(number.charAt(i) == '২') result += '2';
            else if(number.charAt(i) == '৩') result += '3';
            else if(number.charAt(i) == '৪') result += '4';
            else if(number.charAt(i) == '৫') result += '5';
            else if(number.charAt(i) == '৬') result += '6';
            else if(number.charAt(i) == '৭') result += '7';
            else if(number.charAt(i) == '৮') result += '8';
            else if(number.charAt(i) == '৯') result += '9';
            else result += number.charAt(i);
        }
        return result;
    }

    public String makeHijriDate(String month){
        switch (month) {
            case "01":
                return "মুহাররম";
            case "02":
                return "সফর";
            case "03":
                return "রবিউল আউয়াল";
            case "04":
                return "রবিউস সানি";
            case "05":
                return "জমাদিউল আউয়াল";
            case "06":
                return "জমাদিউস সানি";
            case "07":
                return "রজব";
            case "08":
                return "শাবান";
            case "09":
                return "রমজান";
            case "10":
                return "শাওয়ান";
            case "11":
                return "জিলক্বদ";
            case "12":
                return "জিলহজ্জ";
        }
        return "";
    }
}
