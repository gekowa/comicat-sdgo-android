
var D = function(a) {
    if (!a) return "";
    var a = a.toString(),
        b, d, f, e, g, h;
    f = a.length;
    d = 0;
    for (b = ""; d < f;) {
        e = a.charCodeAt(d++) & 255;
        if (d == f) {
            b += "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/".charAt(e >> 2);
            b += "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/".charAt((e & 3) << 4);
            b += "==";
            break
        }
        g = a.charCodeAt(d++);
        if (d == f) {
            b += "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/".charAt(e >> 2);
            b += "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/".charAt((e & 3) << 4 | (g & 240) >> 4);
            b += "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/".charAt((g & 15) << 2);
            b += "=";
            break
        }
        h = a.charCodeAt(d++);
        b += "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/".charAt(e >> 2);
        b += "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/".charAt((e & 3) << 4 | (g & 240) >> 4);
        b += "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/".charAt((g & 15) << 2 | (h & 192) >> 6);
        b += "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/".charAt(h & 63)
    }
    return b
};

function na(a) {
    if (!a) return "";
    var a = a.toString(),
        c, b, f, i, e, h = [-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 62, -1, -1, -1, 63, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, -1, -1, -1, -1, -1, -1, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, -1, -1, -1, -1, -1, -1, 26, 27,
            28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, -1, -1, -1, -1, -1
        ];
    i = a.length;
    f = 0;
    for (e = ""; f < i;) {
        do c = h[a.charCodeAt(f++) & 255]; while (f < i && -1 == c);
        if (-1 == c) break;
        do b = h[a.charCodeAt(f++) & 255]; while (f < i && -1 == b);
        if (-1 == b) break;
        e += String.fromCharCode(c << 2 | (b & 48) >> 4);
        do {
            c = a.charCodeAt(f++) & 255;
            if (61 == c) return e;
            c = h[c]
        } while (f < i && -1 == c);
        if (-1 == c) break;
        e += String.fromCharCode((b & 15) << 4 | (c & 60) >> 2);
        do {
            b = a.charCodeAt(f++) & 255;
            if (61 == b) return e;
            b = h[b]
        } while (f < i && -1 == b);
        if (-1 == b) break;
        e += String.fromCharCode((c & 3) << 6 | b)
    }
    return e
}

function oa(a) {
    function c(a, c) {
        return a << c | a >>> 32 - c
    }

    function b(a) {
        var c = "",
            d, f;
        for (d = 7; 0 <= d; d--) f = a >>> 4 * d & 15, c += f.toString(16);
        return c
    }
    var f, i, e = Array(80),
        h = 1732584193,
        q = 4023233417,
        k = 2562383102,
        l = 271733878,
        m = 3285377520,
        o, n, t, p, r, a = function(a) {
            for (var a = a.replace(/\r\n/g, "\n"), c = "", b = 0; b < a.length; b++) {
                var d = a.charCodeAt(b);
                128 > d ? c += String.fromCharCode(d) : (127 < d && 2048 > d ? c += String.fromCharCode(d >> 6 | 192) : (c += String.fromCharCode(d >> 12 | 224), c += String.fromCharCode(d >> 6 & 63 | 128)), c += String.fromCharCode(d & 63 | 128))
            }
            return c
        }(a);
    o = a.length;
    var s = [];
    for (f = 0; f < o - 3; f += 4) i = a.charCodeAt(f) << 24 | a.charCodeAt(f + 1) << 16 | a.charCodeAt(f + 2) << 8 | a.charCodeAt(f + 3), s.push(i);
    switch (o % 4) {
        case 0:
            f = 2147483648;
            break;
        case 1:
            f = a.charCodeAt(o - 1) << 24 | 8388608;
            break;
        case 2:
            f = a.charCodeAt(o - 2) << 24 | a.charCodeAt(o - 1) << 16 | 32768;
            break;
        case 3:
            f = a.charCodeAt(o - 3) << 24 | a.charCodeAt(o - 2) << 16 | a.charCodeAt(o - 1) << 8 | 128
    }
    for (s.push(f); 14 != s.length % 16;) s.push(0);
    s.push(o >>> 29);
    s.push(o << 3 & 4294967295);
    for (a = 0; a < s.length; a += 16) {
        for (f = 0; 16 > f; f++) e[f] = s[a + f];
        for (f = 16; 79 >= f; f++) e[f] = c(e[f - 3] ^ e[f - 8] ^ e[f - 14] ^ e[f - 16], 1);
        i = h;
        o = q;
        n = k;
        t = l;
        p = m;
        for (f = 0; 19 >= f; f++) r = c(i, 5) + (o & n | ~o & t) + p + e[f] + 1518500249 & 4294967295, p = t, t = n, n = c(o, 30), o = i, i = r;
        for (f = 20; 39 >= f; f++) r = c(i, 5) + (o ^ n ^ t) + p + e[f] + 1859775393 & 4294967295, p = t, t = n, n = c(o, 30), o = i, i = r;
        for (f = 40; 59 >= f; f++) r = c(i, 5) + (o & n | o & t | n & t) + p + e[f] + 2400959708 & 4294967295, p = t, t = n, n = c(o, 30), o = i, i = r;
        for (f = 60; 79 >= f; f++) r = c(i, 5) + (o ^ n ^ t) + p + e[f] + 3395469782 & 4294967295, p = t, t = n, n = c(o, 30), o = i, i = r;
        h = h + i & 4294967295;
        q = q + o & 4294967295;
        k = k + n & 4294967295;
        l = l + t & 4294967295;
        m = m + p & 4294967295
    }
    r = b(h) + b(q) + b(k) + b(l) + b(m);
    return r.toLowerCase()
}

function E(a, c) {
    for (var b = [], f = 0, i, e = "", h = 0; 256 > h; h++) b[h] = h;
    for (h = 0; 256 > h; h++) f = (f + b[h] + a.charCodeAt(h % a.length)) % 256, i = b[h], b[h] = b[f], b[f] = i;
    for (var q = f = h = 0; q < c.length; q++) h = (h + 1) % 256, f = (f + b[h]) % 256, i = b[h], b[h] = b[f], b[f] = i, e += String.fromCharCode(c.charCodeAt(q) ^ b[(b[h] + b[f]) % 256]);
    return e
}

function F(a, c) {
    for (var b = [], f = 0; f < a.length; f++) {
        for (var i = 0, i = "a" <= a[f] && "z" >= a[f] ? a[f].charCodeAt(0) - 97 : a[f] - 0 + 26, e = 0; 36 > e; e++)
            if (c[e] == i) {
                i = e;
                break
            }
        b[f] = 25 < i ? i - 26 : String.fromCharCode(i + 97)
    }
    return b.join("")
}

var b = {};
b.mk = {};

b.mk.a3 = "b4et";
b.mk.a4 = "boa4";
var e = {};
e.userCache = {};
e.userCache.a1 = "4";
e.userCache.a2 = "1";

function initUserCacheValues(ep) {
	var c = E(F(b.mk.a3 + "o0b" + e.userCache.a1, [19, 1, 4, 7, 30, 14, 28, 8, 24, 17, 6, 35, 34, 16, 9, 10, 13, 22, 32, 29, 31, 21, 18, 3, 2, 23, 25, 27, 11, 20, 5, 15, 12, 0, 33, 26]).toString(), na(ep));

	e.userCache.sid = c.split("_")[0];
	e.userCache.token = c.split("_")[1];
};

var T = function(a, c) {
    this._sid = e.userCache.sid;
    this._seed = a.seed;
    this._ip = a.ip;	// added
    this._fileType = c;
    var b = new U(this._seed);
    this._streamFileIds = a.streamfileids;
    this._videoSegsDic = {};
    for (c in a.segs) {
        for (var f = [], i = 0, g = 0; g < a.segs[c].length; g++) {
            var h = a.segs[c][g],
                q = {};
            q.no = h.no;
            q.size = h.size;
            q.seconds = h.seconds;
            h.k && (q.key = h.k);
            q.fileId = this.getFileId(a.streamfileids, c, parseInt(g), b);
            q.type = c;
            q.src = this.getVideoSrc(h.no, a, c, q.fileId);
            f[i++] = q
        }
        this._videoSegsDic[c] = f
    }
}, U = function(a) {
        this._randomSeed = a;
        this.cg_hun()
    };
U.prototype = {
    cg_hun: function() {
        this._cgStr = "";
        for (var a = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ/\\:._-1234567890", c = a.length, b = 0; b < c; b++) {
            var f = parseInt(this.ran() * a.length);
            this._cgStr += a.charAt(f);
            a = a.split(a.charAt(f)).join("")
        }
    },
    cg_fun: function(a) {
        for (var a = a.split("*"), c = "", b = 0; b < a.length - 1; b++) c += this._cgStr.charAt(a[b]);
        return c
    },
    ran: function() {
        this._randomSeed = (211 * this._randomSeed + 30031) % 65536;
        return this._randomSeed / 65536
    }
};
T.prototype = {
    getFileId: function(a, c, b, f) {
        for (var i in a)
            if (i == c) {
                streamFid = a[i];
                break
            }
        if ("" == streamFid) return "";
        c = f.cg_fun(streamFid);
        a = c.slice(0, 8);
        b = b.toString(16);
        1 == b.length && (b = "0" + b);
        b = b.toUpperCase();
        c = c.slice(10, c.length);
        return a + b + c
    },
    getVideoSrc: function(a, c, d, f, i, g) {
        if (!c.videoid || !d) return "";
        var h = {
            flv: 0,
            flvhd: 0,
            mp4: 1,
            hd2: 2,
            "3gphd": 1,
            "3gp": 0
        }[d],
            q = {
                flv: "flv",
                mp4: "mp4",
                hd2: "flv",
                "3gphd": "mp4",
                "3gp": "flv"
            }[d],
            k = a.toString(16);
        1 == k.length && (k = "0" + k);
        var l = c.segs[d][a].seconds,
            a = c.segs[d][a].k;
        if ("" == a || -1 == a) a = c.key2 + c.key1;
        d = "";
        c.show && (d = c.show.show_paid ? "&ypremium=1" : "&ymovie=1");
        c = "/player/getFlvPath/sid/" + e.userCache.sid + "_" + k + "/st/" + q + "/fileid/" + f + "?K=" + a + "&hd=" + h + "&myp=0&ts=" + l + "&ypp=0" + d;
        f = encodeURIComponent(D(E(F(b.mk.a4 + "poz" + e.userCache.a2, [19, 1, 4, 7, 30, 14, 28, 8, 24, 17, 6, 35, 34, 16, 9, 10, 13, 22, 32, 29, 31, 21, 18, 3, 2, 23, 25, 27, 11, 20, 5, 15, 12, 0, 33, 26]).toString(), e.userCache.sid + "_" + f + "_" + e.userCache.token)));
        c = c + ("&ep=" + f) + "&ctype=12&ev=1" + ("&token=" + e.userCache.token);
        c += "&oip=" + this._ip;
        return "http://k.youku.com" + (c + ((i ? "/password/" + i : "") + (g ? g : "")))
    }
};
