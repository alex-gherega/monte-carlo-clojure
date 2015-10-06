(ns ^{:doc "Compute the PI number through a Monte-Carlo aproach"
      :author "avr.PhD. alex.gherega@gmail.com"} monte-carlo-clojure.pi
      (:use [monte-carlo-clojure.core])
      (:require [clojure.tools.logging :as log]))

(defn gen-rand-point
  ([] (gen-rand-point 1 1))
  ([max-x max-y]
   [(rand max-x) (rand max-y)]))

(defn is-in-circle?
  "check if (x - circle-center-x) ^2 + (y - circle-center-y) ^2 < radius ^2"
  ([[x y :as point] [xc yc :as circle-center] r]
   (let [axis-fn #(sqr (- %1 %2))]
     (<= (+ (axis-fn x xc) (axis-fn y yc))
         (sqr r))))
  ([[x y :as point]]
   (is-in-circle? point [0.5 0.5] 0.5)))

(defn pi-fn [n-max n-circle]
  (* 4. (/ n-circle n-max)))

(defn whenc-inc [[x y :as point] n]
  (if (is-in-circle? point)
    (inc n)
    n))

(defn- gather-circle-points [n]
  (loop [index n
         nc 0]
    (if (zero? index)
      nc
      (recur (dec index)
             (whenc-inc (gen-rand-point) nc)))))

(defn monte-carlo-pi [n]
  (pi-fn n (gather-circle-points n)))

(defn gen-worker-data [n div]
  "generate a vector of n/div size div; n must be bigger then div"
  (vec (repeat div (long (/ n div)))))

(defn pmonte-carlo-pi [n div]
  (pi-fn n
         (reduce + (pmap gather-circle-points
                         (gen-worker-data n div)))))

(defn performance-test
  ([] (performance-test 100000000))
  ([n]
   (list "[sequential-run] " (time (monte-carlo-pi n))
         "[parallel-run] " (time (pmonte-carlo-pi n 33)))))
