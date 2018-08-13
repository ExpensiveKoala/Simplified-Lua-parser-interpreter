function p ()
    a = 0
    b = 0
    while ~= a 1 do
        repeat
            if >= b 5 then
                a = + a 1
            else
                if == b 1 then
                    print(b)
                else
                    print(a)
                end
                if <= b 2 then
                    print(123)
                else
                    print(456)
                end
                if > b 2 then
                    print(321)
                else
                    print(654)
                end
            end
            b = + b 1
        until == b 10
    end
end