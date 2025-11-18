$(document).ready(function () {
    var analogClock = PF('analogClockWidget');
    var clock = PF('clockWidget');
    var date= PF('dateWidget');

    if (!clock || !analogClock || !date) return;

    analogClock.stop();
    date.stop();
    clock.stop();

    analogClock.sync = function() {
        this.isAnalogClock() || this.stop();
        var a = this;
        PrimeFaces.ajax.Request.handle({
            source: this.id,
            process: this.id,
            async: !0,
            global: !1,
            params: [{
                name: this.id + "_sync",
                value: !0
            }],
            oncomplete: function(c, d, g, l) {
                a.current = new Date(g.datetime)
                a.update();
            }
        })
    };

    analogClock.start = function() {
        var a = this;
        a.updateOutput();
    }
    clock.start = analogClock.start;
    date.start = analogClock.start;

    analogClock.sync();
    date.sync();
    clock.sync();


    analogClock.clock.attr({
        fill: color_dark,
        stroke: color_light,
        "stroke-width": 3
    });

    analogClock.hour_hand.attr({ stroke: color_light, "stroke-width": 4 });
    analogClock.minute_hand.attr({ stroke: color_light, "stroke-width": 3 });
    analogClock.second_hand.attr({ stroke: color_red_bright, "stroke-width": 2 });

    analogClock.pin.attr({ fill: color_light });

    for (var i = 0; i < analogClock.hour_sign.length; i++) {
        analogClock.hour_sign[i].attr({ stroke: color_light, "stroke-width": 1.5 });
    }

});
