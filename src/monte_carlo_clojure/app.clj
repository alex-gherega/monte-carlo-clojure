(ns ^{:doc "Main app" :author "avr.PhD. alex.gherega@gmail.com"}
  monte-carlo-clojure.app
  (:require [monte-carlo-clojure.pi :as pi]))

(defn -main []
  (pi/performance-test))
