(ns clojurewerkz.money.amounts-test
  (:use clojure.test
        clojurewerkz.money.amounts)
  (:import [org.joda.money CurrencyUnit Money]
           [java.math RoundingMode]))


(println (str "Using Clojure version " *clojure-version*))

(deftest test-amount-of-with-currency-unit-and-double
  (let [^Money money (amount-of CurrencyUnit/EUR 10.00)]
    (is (= (.getCurrencyUnit money) CurrencyUnit/EUR))
    (is (= 10.00M (.getAmount money)))))

(deftest test-amount-of-with-currency-unit-and-double-and-rounding-mode
  (let [^Money money (amount-of CurrencyUnit/EUR 10.0333333 RoundingMode/DOWN)]
    (is (= (.getCurrencyUnit money) CurrencyUnit/EUR))
    (is (= 10.03M (.getAmount money)))))



(deftest test-amount-of-with-currency-unit-and-amount-in-major-units
  (are [cu amount bdec] (let [^Money money (of-major cu amount)]
                          (is (= (.getCurrencyUnit money) cu))
                          (is (= bdec (.getAmount money))))
       CurrencyUnit/USD 30 30.00M
       CurrencyUnit/GBP 40 40.00M
       CurrencyUnit/JPY 50 50.M))


(deftest test-amount-of-with-currency-unit-and-amount-in-minor-units
  (are [cu amount bdec] (let [^Money money (of-minor cu amount)]
                          (is (= (.getCurrencyUnit money) cu))
                          (is (= bdec (.getAmount money))))
       CurrencyUnit/USD 2595 25.95M
       CurrencyUnit/GBP 4012 40.12M
       CurrencyUnit/JPY 5000 5000M))


(deftest test-zero-amount
  (are [cu bdec] (let [^Money money (zero cu)]
                          (is (= (.getCurrencyUnit money) cu))
                          (is (= bdec (.getAmount money))))
       CurrencyUnit/USD 0.00M
       CurrencyUnit/GBP 0.00M
       CurrencyUnit/JPY 0M))

(deftest test-money-total
  (let [cu CurrencyUnit/EUR
        a  (amount-of cu 15.00)
        b  (of-major  cu 10)
        c  (of-minor  cu 1300)
        d  (zero      cu)
        t  (total [a b c d])]
    (is (= (.getCurrencyUnit t) cu))
    (is (= 38.00M (.getAmount t)))))
