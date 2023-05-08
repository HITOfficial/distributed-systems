module Demo {
    interface FUWS {
    // Frequently Used, With State object
    int incrementAndGet();
    };

    interface RUWS {
    // Rarely Used, With State object
    void saveALotOfData(string data);
    };

    interface FUWOS {
    // Frequently Used, Without State object
    idempotent string concatWords(string first, string second);
    };
};