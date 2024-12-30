'use client';

import Content from "@/components/Content";
import Table, {PagedData} from "@/components/Table";

const Index = () => {
    return (
        <Content title={`Index`}>
            <Table data={{
                content: [{
                    "#": 0,
                    username: `admin`,
                    password: `admin`,
                    email: ``,
                    roles: [`admin`].join(`, `)
                }, {
                    "#": 1,
                    username: `squall`,
                    password: `Squall-1234`,
                    email: `squall@localhost`,
                    roles: [`item:write`, `itemInventory:write`, `weapon:write`, `weaponRemodel:write`, `userProfile:write`].join(`, `)
                }, {
                    "#": 2,
                    username: `zell`,
                    password: `Zell-1234`,
                    email: `zell@localhost`,
                    roles: [`item:read`, `itemInventory:read`, `weapon:read`, `weaponRemodel:read`, `userProfile:read`].join(`, `)
                }, {
                    "#": 3,
                    username: `irvine`,
                    password: `Irvine-1234`,
                    email: `irvine@localhost`,
                    roles: [`item:read`, `itemInventory:read`, `weapon:read`, `weaponRemodel:read`, `userProfile:read`].join(`, `)
                }, {
                    "#": 4,
                    username: `quistis`,
                    password: `Quistis-1234`,
                    email: `quistis@localhost`,
                    roles: [].join(`, `)
                }, {
                    "#": 5,
                    username: `rinoa`,
                    password: `Rinoa-1234`,
                    email: `rinoa@localhost`,
                    roles: [].join(`, `)
                }, {
                    "#": 6,
                    username: `selphie`,
                    password: `Selphie-1234`,
                    email: `selphie@localhost`,
                    roles: [].join(`, `)
                }, ]
            } as PagedData<any>}/>
        </Content>
    );
}

export default Index;
