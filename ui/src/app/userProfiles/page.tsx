'use client';

import {useEffect, useState} from "react";
import Content from "@/components/Content";
import Table, {PagedData} from "@/components/Table";
import {useAuth} from "@/contexts/AuthContext";
import Modal from "@/components/Modal";
import Fetch from "@/utils/Fetch";

type UserProfile = {
    id: number;
    username: string;
    familyName: string;
    givenName: string;
    age: number;
    gender: string;
    profile: string;
};

type ItemInventoryData = {
    id: number;
    itemId: number;
    name: string;
    quantity: number;
};


const UserProfiles = () => {
    const {user} = useAuth();
    const [userProfiles, setUserProfiles] = useState<PagedData<UserProfile> | null>()
    const [editModalShown, setEditModalShown] = useState(false);
    const [editModalData, setEditModalData] = useState<any | null | undefined>(null);
    const [addModalShown, setAddModalShown] = useState(false);
    const [addModalData, setAddModalData] = useState<any | null | undefined>(null);
    const [fetch, setFetch] = useState(new Fetch(user?.access_token));

    useEffect(() => {
        setFetch(new Fetch(user?.access_token));
    }, [user?.access_token]);

    useEffect(() => {
        const fetchUsers = async () => {
            try {
                setUserProfiles(await fetch.to(`http://localhost:8080/userProfiles`));
            } catch (e) {
                console.error(e);
            }
        };

        try {
            void fetchUsers();
        } catch (e) {
            console.error(e);
        }
    }, [user?.profile?.preferred_username]);

    return (
        <Content title={`User Profiles`}>
            <Table<UserProfile>
                data={userProfiles}
                searchLabel={`Username`}
                onSearch={async (v) => {
                    try {
                        setUserProfiles(await fetch.to(`http://localhost:8080/userProfiles?username=${encodeURIComponent(v)}`));
                    } catch (e) {
                        console.error(e);
                    }
                }}
                onAdd={() => {
                    setAddModalData({
                        id: ``,
                        username: ``,
                        familyName: ``,
                        givenName: ``,
                        age: ``,
                        gender: ``,
                        profile: ``,
                    });

                    setAddModalShown(true);
                }}
                onEdit={(data) => {
                    setEditModalData(data);

                    setEditModalShown(true);
                }}
                onClickPage={async (keyword, page) => {
                    try {
                        setUserProfiles(await fetch.to(`http://localhost:8080/userProfiles?username=${encodeURIComponent(keyword)}&page=${page}`));
                    } catch (e) {
                        console.error(e);
                    }
                }}
            />
            <Modal open={editModalShown}
                   data={editModalData}
                   readonly={[`id`, `username`]}
                   title={`Edit User Profile`}
                   onSave={async (data) => {
                       const keepContent = (userProfiles?.content ?? [])
                           .filter(d => data.id !== d.id);
                       const newContentItem = {
                           ...(((userProfiles?.content ?? [])).find(d => data.id === d.id)!),
                           username: data.username,
                           familyName: data.familyName,
                           givenName: data.givenName,
                           age: data.age,
                           gender: data.gender,
                           profile: data.profile,
                       }

                       if (!newContentItem) {
                           return;
                       }

                       try {
                           const updateUserProfile = {
                               id: newContentItem.id,
                               familyName: newContentItem.familyName,
                               givenName: newContentItem.givenName,
                               age: newContentItem.age,
                               gender: newContentItem.gender,
                               profile: newContentItem.profile,
                           }

                           //?username=${encodeURIComponent(v)}
                           await fetch.to(`http://localhost:8080/userProfiles?username=${encodeURIComponent(newContentItem.username)}`, {
                               method: `PATCH`,
                               headers: {
                                   "Content-Type": "application/merge-patch+json",
                               },
                               body: JSON.stringify(updateUserProfile),
                           });

                           setUserProfiles(await fetch.to(`http://localhost:8080/userProfiles`));
                           setEditModalShown(false);
                       } catch (e) {
                           console.error(e);
                       }
                   }}
                   onCancel={() => {
                       setEditModalShown(false);
                   }}
            />
            <Modal open={addModalShown}
                   data={addModalData}
                   title={`Add User Profile`}
                   onSave={async (data) => {
                       if (!data.id) {
                           return;
                       }

                       try {
                           const newData = await fetch.to(`http://localhost:8080/userProfiles`, {
                               method: `POST`,
                               body: JSON.stringify({
                                   id: data.id,
                                   username: data.username,
                                   familyName: data.familyName,
                                   givenName: data.givenName,
                                   age: data.age,
                                   gender: data.gender,
                                   profile: data.profile,
                               }),
                           });

                           setUserProfiles({
                               ...userProfiles,
                               content: [
                                   ...(userProfiles?.content ?? []),
                                   newData,
                               ]
                           } as PagedData<UserProfile>);

                           setAddModalShown(false);
                       } catch (e) {
                           console.error(e);
                       }
                   }}
                   onCancel={() => {
                       setAddModalShown(false);
                   }}
            />
        </Content>
    );
}

export default UserProfiles;
