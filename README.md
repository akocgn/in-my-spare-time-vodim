# in-my-spare-time-vodim

Sometimes I need to watch the published metrics values of some components. I do not want to involve icinga, graphite etc.
So I wrote a very lightweight (aka simple) tool to display dropwizard.io's metrics json output in charts. 

It polls a web resource for json files containing gauges and meters, groups them into (JavaFX) charts. Polls continously (like every 30 seconds)
and updates the charts.

Data source is provided with a command line argument: --src=http://my-json-source:1234 

Sole purpose is display without any persistence, notifications etc. So I called it Vo(latile)Di(splay of)M(etrics)...

