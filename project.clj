(defproject nonprofits "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.7.0-beta2"]
                 [org.clojure/data.csv "0.1.2"]
                 [iota "1.1.2"]
                 [cheshire "5.4.0"]]
  :main ^:skip-aot nonprofits.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}}
  :jvm-opts ["-Xmx4096m", "-Xms1024m"])
